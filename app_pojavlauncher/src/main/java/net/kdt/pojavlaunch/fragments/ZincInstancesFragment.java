package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import git.artdeell.mojo.R;
import net.kdt.pojavlaunch.instances.DisplayInstance;
import net.kdt.pojavlaunch.instances.Instances;

import java.io.IOException;

public class ZincInstancesFragment extends Fragment {

    public static final String TAG = "ZINC_INSTANCES_FRAGMENT";

    private LinearLayout instancesContainer;
    private TextView instancesCount;

    public ZincInstancesFragment() {
        super(R.layout.zinc_instances);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        instancesContainer =
                view.findViewById(R.id.zinc_instances_container);

        instancesCount =
                view.findViewById(R.id.zinc_instances_count);

        Button newInstanceButton =
                view.findViewById(R.id.zinc_new_instance_button);

        newInstanceButton.setOnClickListener(v ->
                Toast.makeText(
                        requireContext(),
                        "New Instance - coming soon",
                        Toast.LENGTH_SHORT
                ).show()
        );

        loadInstances();
    }

    private void loadInstances() {
        try {
            Instances instances = Instances.loadDisplay();

            instancesContainer.removeAllViews();

            int count = instances.list.size();

            instancesCount.setText(
                    count + (count == 1 ? " instance" : " instances")
            );

            for (int i = 0; i < count; i++) {
                DisplayInstance instance = instances.list.get(i);

                boolean selected =
                        i == instances.selectedIndex;

                addInstanceCard(instance, selected);
            }

        } catch (IOException e) {
            instancesCount.setText("Failed to load instances");

            Toast.makeText(
                    requireContext(),
                    "Could not load instances",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void addInstanceCard(
            DisplayInstance instance,
            boolean selected
    ) {
        LinearLayout card = new LinearLayout(requireContext());

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 18, 20, 18);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(0, 0, 0, 12);

        card.setLayoutParams(cardParams);

        TextView name = new TextView(requireContext());

        String instanceName = instance.name;

        if (instanceName == null || instanceName.trim().isEmpty()) {
            instanceName = "Unnamed Instance";
        }

        name.setText(instanceName);
        name.setTextColor(0xFFFFFFFF);
        name.setTextSize(18);

        TextView version = new TextView(requireContext());

        String versionId = instance.versionId;

        if (versionId == null || versionId.trim().isEmpty()) {
            versionId = "Unknown version";
        }

        version.setText(versionId);
        version.setTextColor(0xFF999999);
        version.setTextSize(14);

        TextView status = new TextView(requireContext());

        if (selected) {
            status.setText("✓ SELECTED");
            status.setTextColor(0xFFFFFFFF);
        } else {
            status.setText("TAP TO SELECT");
            status.setTextColor(0xFF777777);
        }

        status.setTextSize(13);

        card.addView(name);
        card.addView(version);
        card.addView(status);

        card.setBackgroundColor(
                selected
                        ? 0xFF242424
                        : 0xFF171717
        );

        card.setOnClickListener(v -> {
            Instances.setSelectedInstance(instance);

            Toast.makeText(
                    requireContext(),
                    "Selected " + instanceName,
                    Toast.LENGTH_SHORT
            ).show();

            loadInstances();
        });

        instancesContainer.addView(card);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (instancesContainer != null) {
            loadInstances();
        }
    }
}