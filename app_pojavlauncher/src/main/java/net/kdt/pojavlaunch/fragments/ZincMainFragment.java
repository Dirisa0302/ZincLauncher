package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import git.artdeell.mojo.R;
import net.kdt.pojavlaunch.LauncherActivity;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.prefs.screens.LauncherPreferenceFragment;

public class ZincMainFragment extends Fragment {

    public ZincMainFragment() {
        super(R.layout.zinc_main);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        // PLAY
        Button playButton = view.findViewById(R.id.zinc_play_button);

        playButton.setOnClickListener(v ->
                ExtraCore.setValue(
                        ExtraConstants.LAUNCH_GAME,
                        true
                )
        );

        // ACCOUNT
        TextView accountButton =
                view.findViewById(R.id.zinc_account_button);

        accountButton.setOnClickListener(v ->
                ExtraCore.setValue(
                        ExtraConstants.SELECT_AUTH_METHOD,
                        true
                )
        );

        // SETTINGS
        TextView settingsButton =
                view.findViewById(R.id.zinc_settings_button);

        settingsButton.setOnClickListener(v -> {
            FragmentManager manager =
                    requireActivity().getSupportFragmentManager();

            if (manager.isStateSaved()) return;

            Tools.swapFragment(
                    requireActivity(),
                    LauncherPreferenceFragment.class,
                    LauncherActivity.SETTING_FRAGMENT_TAG,
                    null
            );
        });
    }
}