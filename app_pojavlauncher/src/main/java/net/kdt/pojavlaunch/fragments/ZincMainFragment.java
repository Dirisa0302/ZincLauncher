package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import git.artdeell.mojo.R;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;

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

        Button playButton = view.findViewById(R.id.zinc_play_button);

        playButton.setOnClickListener(v ->
                ExtraCore.setValue(
                        ExtraConstants.LAUNCH_GAME,
                        true
                )
        );
    }
}