package tsd.boss_launcher.app_list_screen;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import tsd.boss_launcher.R;
import tsd.boss_launcher.app_list_data.AppListViewModel;


/**
 * this fragment show app list
 *
 * @author gaopeng
 */
public class AppListFragment extends Fragment {
    static final int APP_ICON_COLUMN = 5;
    List<ResolveInfo> appInfos = new ArrayList<>(APP_ICON_COUNT);
    ViewPager viewPager;
    SparseArray<View> recyclerViews = new SparseArray<>();
    List<RecyclerView.Adapter> adapters = new ArrayList<>();
    TextView pageNumText;
    AppListViewModel appListViewModel;
    AppListActivity appListActivity;
    static final int APP_ICON_ROW = 3;
    static final int APP_ICON_COUNT = APP_ICON_COLUMN * APP_ICON_ROW;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appListActivity = new AppListActivity();
        appListViewModel = ViewModelProviders.of(getActivity()).get(AppListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        handleViewPager(view);
        handlePageNumText(view);
    }

    private void handlePageNumText(final View view) {
        pageNumText = view.findViewById(R.id.pageNumText);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                refreshPageNumText();
            }
        });
    }

    private void refreshPageNumText() {
        int position = viewPager.getCurrentItem();
        pageNumText.setText("" + (position + 1) + "/" + viewPager.getAdapter().getCount());
    }

    private void refreshAppList(final List<ResolveInfo> oldList, final List<ResolveInfo> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).activityInfo.packageName.equals(newList.get(newItemPosition).activityInfo.packageName);
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ResolveInfo oldInfo = oldList.get(oldItemPosition);
                ResolveInfo newInfo = newList.get(newItemPosition);
                return oldInfo.activityInfo.name.equals(newInfo.activityInfo.name) && oldInfo.activityInfo.icon == newInfo.activityInfo.icon;
            }
        }, true);
        appInfos = newList;
        viewPager.getAdapter().notifyDataSetChanged();
        refreshPageNumText();
        for (RecyclerView.Adapter a : adapters) {
//            diffResult.dispatchUpdatesTo(a);
            a.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        listenAppListChange();
    }

    @Override
    public void onStop() {
        super.onStop();
        appListViewModel.getAppInfosLiveData().removeObservers(this);
    }

    private void listenAppListChange() {
        appListViewModel.getAppInfosLiveData().observe(this, new Observer<List<ResolveInfo>>() {
            @Override
            public void onChanged(List<ResolveInfo> infos) {
                ArrayList<ResolveInfo> oldList = new ArrayList<>(appInfos);
                if (infos == null) {
                    refreshAppList(oldList, new ArrayList<ResolveInfo>());
                } else {
                    refreshAppList(oldList, infos);
                }
            }
        });
    }

    private int getPageCount() {
        return (int) Math.ceil(appInfos.size() * 1f / APP_ICON_COUNT);
    }

    private void handleViewPager(final View view) {
        viewPager = view.findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return getPageCount();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View item;
                container.addView(item = initAppListView(position));
                recyclerViews.put(position, item);
                return item;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                adapters.remove(((RecyclerView) container.getChildAt(position)).getAdapter());
                container.removeView(recyclerViews.get(position));
                recyclerViews.remove(position);
            }

        };
        viewPager.setAdapter(pagerAdapter);
    }

    private View initAppListView(int pagePosition) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), isLandscape() ? APP_ICON_COLUMN : APP_ICON_ROW) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVerticalScrollBarEnabled(false);
        int padding = (int) (getResources().getDisplayMetrics().widthPixels * 0.0260);
        recyclerView.setPaddingRelative(padding, 0, padding, 0);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.HORIZONTAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        AppListAdapter appListAdapter = new AppListAdapter(pagePosition);
        recyclerView.setAdapter(appListAdapter);
        adapters.add(appListAdapter);
        return recyclerView;
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppIconHolder> {
        int pagePosition;
        int itemWidth, itemHeight, iconRadius;

        public AppListAdapter(int pagePosition) {
            this.pagePosition = pagePosition;
        }

        class AppIconHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            ImageView imageView;
            TextView textView;

            public AppIconHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
                textView = itemView.findViewById(R.id.textView);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int start = APP_ICON_COUNT * pagePosition + getAdapterPosition();
                ResolveInfo resolveInfo = appInfos.get(start);
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                intent.setComponent(componentName);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                IconMenuFragment iconMenuFragment = new IconMenuFragment();
                Bundle bundle = new Bundle();
                int start = APP_ICON_COUNT * pagePosition + getAdapterPosition();
                ResolveInfo resolveInfo = appInfos.get(start);
                bundle.putParcelable(IconMenuFragment.APP_RESOLVE_INFO, resolveInfo);
                iconMenuFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().add(R.id.root_container, iconMenuFragment).addToBackStack("").commit();
                return true;
            }
        }

        @NonNull
        @Override
        public AppIconHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_icon_list, parent, false);
            itemWidth = parent.getWidth() / (isLandscape() ? APP_ICON_COLUMN : APP_ICON_ROW);
            itemHeight = parent.getHeight() / (isLandscape() ? APP_ICON_ROW : APP_ICON_COLUMN);
            iconRadius = (int) (Math.min(itemWidth, itemHeight) / 2.2);
            return new AppIconHolder(item);
        }

        @Override
        public void onBindViewHolder(@NonNull AppIconHolder holder, int position) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = itemHeight;
            holder.itemView.setLayoutParams(layoutParams);
            holder.imageView.getLayoutParams().width = iconRadius;
            holder.imageView.getLayoutParams().height = iconRadius;
            int start = APP_ICON_COUNT * pagePosition + holder.getAdapterPosition();
            ResolveInfo resolveInfo = appInfos.get(start);
            holder.imageView.setImageDrawable(resolveInfo.loadIcon(getActivity().getPackageManager()));
            holder.textView.setText(resolveInfo.loadLabel(getActivity().getPackageManager()));
        }

        @Override
        public int getItemCount() {
            int start = APP_ICON_COUNT * pagePosition;
            int remain = appInfos.size() - start;
            return remain > APP_ICON_COUNT ? APP_ICON_COUNT : remain;
        }

    }

}
