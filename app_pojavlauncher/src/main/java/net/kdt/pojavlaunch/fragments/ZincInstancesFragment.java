package net.kdt.pojavlaunch.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import git.artdeell.mojo.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.instances.DisplayInstance;
import net.kdt.pojavlaunch.instances.Instance;
import net.kdt.pojavlaunch.instances.InstanceIconProvider;
import net.kdt.pojavlaunch.instances.Instances;

import java.io.IOException;

public class ZincInstancesFragment extends Fragment {

    public static final String TAG = "ZINC_INSTANCES_FRAGMENT";
    public static final String ARG_RETURN_TO_ZINC_INSTANCES =
            "zinc_return_to_instances";

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

        newInstanceButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putBoolean(ARG_RETURN_TO_ZINC_INSTANCES, true);

            Tools.swapFragment(
                    requireActivity(),
                    ProfileTypeSelectFragment.class,
                    ProfileTypeSelectFragment.TAG,
                    args
            );
        });

        loadInstances();
    }

    private void loadInstances() {
        if (!isAdded() || instancesContainer == null) return;

        try {
            Instances instances = Instances.loadDisplay();

            instancesContainer.removeAllViews();

            int count = instances.list.size();

            instancesCount.setText(
                    count + (count == 1 ? " instance" : " instances")
            );

            for (int i = 0; i < count; i++) {
                DisplayInstance instance = instances.list.get(i);

                boolean selected = i == instances.selectedIndex;

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

        String instanceName = instance.name;

        if (instanceName == null || instanceName.trim().isEmpty()) {
            instanceName = "Unnamed Instance";
        }

        String versionId = instance.versionId;

        if (versionId == null || versionId.trim().isEmpty()) {
            versionId = "Unknown version";
        }

        TextView name = new TextView(requireContext());
        name.setText(instanceName);
        name.setTextColor(0xFFFFFFFF);
        name.setTextSize(18);
        name.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView version = new TextView(requireContext());
        version.setText(versionId);
        version.setTextColor(0xFF999999);
        version.setTextSize(14);

        TextView status = new TextView(requireContext());
        status.setText(selected ? "✓ SELECTED" : "TAP TO SELECT");
        status.setTextColor(selected ? 0xFFFFFFFF : 0xFF777777);
        status.setTextSize(13);

        card.addView(name);
        card.addView(version);
        card.addView(status);

        card.setBackgroundColor(
                selected ? 0xFF242424 : 0xFF171717
        );

        final String displayName = instanceName;

        // SELECT
        card.setOnClickListener(v -> {
            try {
                Instances.setSelectedInstance(instance);

                Toast.makeText(
                        requireContext(),
                        "Selected " + displayName,
                        Toast.LENGTH_SHORT
                ).show();

                loadInstances();

            } catch (Exception e) {
                Toast.makeText(
                        requireContext(),
                        "Could not select instance",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        // ACTION BUTTONS
        LinearLayout actions = new LinearLayout(requireContext());
        actions.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams actionsParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        actionsParams.topMargin = 10;
        actions.setLayoutParams(actionsParams);

        Button editButton = new Button(requireContext());
        editButton.setText("EDIT");

        Button deleteButton = new Button(requireContext());
        deleteButton.setText("DELETE");

        LinearLayout.LayoutParams editParams =
                new LinearLayout.LayoutParams(
                        0, 48, 1f
                );

        LinearLayout.LayoutParams deleteParams =
                new LinearLayout.LayoutParams(
                        0, 48, 1f
                );

        deleteParams.leftMargin = 8;

        actions.addView(editButton, editParams);
        actions.addView(deleteButton, deleteParams);
        card.addView(actions);

        // EDIT
        editButton.setOnClickListener(v -> {
            try {
                Instances.setSelectedInstance(instance);

                Bundle args = new Bundle();
                args.putBoolean(ARG_RETURN_TO_ZINC_INSTANCES, true);

                Tools.swapFragment(
                        requireActivity(),
                        InstanceEditorFragment.class,
                        InstanceEditorFragment.TAG,
                        args
                );

            } catch (Exception e) {
                Toast.makeText(
                        requireContext(),
                        "Could not open instance editor",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        // DELETE
        deleteButton.setOnClickListener(v ->
                confirmDelete(instance, displayName)
        );

        instancesContainer.addView(card);
    }

    private void confirmDelete(
            DisplayInstance displayInstance,
            String instanceName
    ) {
        try {
            Instances instances = Instances.loadDisplay();

            if (instances.list.size() <= 1) {
                Toast.makeText(
                        requireContext(),
                        "Keep at least one instance",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
        } catch (IOException e) {
            Toast.makeText(
                    requireContext(),
                    "Could not check instances",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete instance?")
                .setMessage(
                        "Delete \"" + instanceName
                                + "\"? This cannot be undone."
                )
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("DELETE", (dialog, which) -> {
                    try {
                        // Load the exact instance selected from this card.
                        Instances.setSelectedInstance(displayInstance);

                        Instance target = Instances.loadSelectedInstance();

                        if (target == null) {
                            Toast.makeText(
                                    requireContext(),
                                    "Could not find instance",
                                    Toast.LENGTH_LONG
                            ).show();
                            return;
                        }

                        InstanceIconProvider.dropIcon(target);
                        Instances.removeInstance(target);

                        Toast.makeText(
                                requireContext(),
                                "Instance deleted",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadInstances();

                    } catch (Exception e) {
                        Toast.makeText(
                                requireContext(),
                                "Could not delete instance",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                })
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (instancesContainer != null) {
            loadInstances();
        }
    }
}