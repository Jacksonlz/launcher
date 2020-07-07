package tsd.boss_launcher.app_list_screen;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import tsd.boss_launcher.R;


/**
 * long press icon popup this fragment.
 *
 * @author gaopeng
 */
public class IconMenuFragment extends Fragment {
    public static final String APP_RESOLVE_INFO = "app_resolve_info";
    ResolveInfo resolveInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new AutoTransition());
        setExitTransition(new AutoTransition());
        if (getArguments() != null) {
            resolveInfo = getArguments().getParcelable(APP_RESOLVE_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_icon_menu, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        handleOutsideClick(view);
        handleAppName(view);
        handleUninstallButton(view);
    }

    private void handleOutsideClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void handleAppName(View view) {
        if (resolveInfo == null) {
            return;
        }
        TextView textView = view.findViewById(R.id.app_name);
        textView.setText(resolveInfo.loadLabel(getActivity().getPackageManager()));
    }

    private void handleUninstallButton(View view) {
        view.findViewById(R.id.uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resolveInfo == null) {
                    return;
                }
                Uri uri = Uri.parse("package:" + resolveInfo.activityInfo.packageName);
                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                startActivity(intent);
                dismiss();
            }
        });
    }

    private void dismiss() {

        getFragmentManager().beginTransaction().remove(IconMenuFragment.this).commit();
    }
}
