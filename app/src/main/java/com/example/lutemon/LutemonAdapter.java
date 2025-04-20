package com.example.lutemon;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LutemonAdapter extends RecyclerView.Adapter<LutemonAdapter.LutemonViewHolder> {
    private Context context;
    private ArrayList<Lutemon> lutemons;
    private String currentLocation;

    public LutemonAdapter(Context context, ArrayList<Lutemon> lutemons, String currentLocation) {
        this.context = context;
        this.lutemons = lutemons;
        this.currentLocation = currentLocation;
    }

    @NonNull
    @Override
    public LutemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lutemon_item, parent, false);
        return new LutemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LutemonViewHolder holder, int position) {
        Lutemon lutemon = lutemons.get(position);

        // Set the image
        holder.imgLutemon.setImageResource(lutemon.getImageResource());

        holder.txtName.setText(lutemon.getName() + " (" + lutemon.getColor() + ")");

        // Show both combat stats and performance stats
        holder.txtStats.setText("ATK: " + lutemon.getAttack() + " DEF: " + lutemon.getDefense() +
                " HP: " + lutemon.getHealth() + "/" + lutemon.getMaxHealth() +
                " EXP: " + lutemon.getExperience());

        // Add a line for performance stats
        holder.txtStats.append("\n" + lutemon.getStatsString());

        // Configure buttons based on current location
        switch (currentLocation) {
            case "home":
                holder.btnAction1.setText("Move to Training");
                holder.btnAction2.setText("Move to Battle");

                holder.btnAction1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);
                            TrainingArea.getInstance().addLutemon(lutemon);
                            Home.getInstance().removeLutemon(lutemon.getId());
                            lutemons.remove(position);
                            notifyItemRemoved(position);
                            checkEmpty();
                        }
                    }
                });

                holder.btnAction2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);
                            BattleField.getInstance().addLutemon(lutemon);
                            Home.getInstance().removeLutemon(lutemon.getId());
                            lutemons.remove(position);
                            notifyItemRemoved(position);
                            checkEmpty();
                        }
                    }
                });
                break;

            case "training":
                holder.btnAction1.setText("Train");
                holder.btnAction2.setText("Move to Home");

                holder.btnAction1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);

                            // Store current stats to detect what changed
                            int oldAttack = lutemon.getAttack();
                            int oldDefense = lutemon.getDefense();
                            int oldMaxHealth = lutemon.getMaxHealth();

                            // Train the Lutemon (this will randomly improve a stat)
                            lutemon.addExperience();

                            // Record training day
                            lutemon.recordTrainingDay();

                            // Determine which stat was improved
                            String trainingResult;
                            if (lutemon.getAttack() > oldAttack) {
                                trainingResult = "Attack +1";
                            } else if (lutemon.getDefense() > oldDefense) {
                                trainingResult = "Defense +1";
                            } else if (lutemon.getMaxHealth() > oldMaxHealth) {
                                trainingResult = "Max Health +2";
                            } else {
                                trainingResult = "Experience +1";
                            }

                            Toast.makeText(context,
                                    lutemon.getName() + " gained: " + trainingResult,
                                    Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                        }
                    }
                });

                holder.btnAction2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);
                            Home.getInstance().addLutemon(lutemon);
                            TrainingArea.getInstance().removeLutemon(lutemon.getId());
                            lutemons.remove(position);
                            notifyItemRemoved(position);
                            checkEmpty();
                        }
                    }
                });
                break;

            case "battle":
                holder.btnAction1.setText("Select for Battle");
                holder.btnAction2.setText("Move to Home");

                holder.btnAction1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);
                            if (context instanceof BattleActivity) {
                                BattleActivity activity = (BattleActivity) context;

                                // Check if this item is already selected
                                boolean isCurrentlySelected = holder.btnAction1.getText().equals("Deselect");

                                // If we're selecting this item, first notify the activity to deselect any previous selection
                                if (!isCurrentlySelected) {
                                    // This will cause any previously selected item to be deselected
                                    activity.deselectLutemon(-1); // -1 is a special value that means "deselect all"

                                    // Then select this one
                                    activity.selectLutemonForBattle(lutemon.getId());
                                    holder.btnAction1.setText("Deselect");
                                    holder.itemView.setBackgroundColor(0x33FF0000); // Light red background
                                } else {
                                    // Deselect this Lutemon
                                    activity.deselectLutemon(lutemon.getId());
                                    holder.btnAction1.setText("Select for Battle");
                                    holder.itemView.setBackgroundColor(0xFFFFFFFF); // White background
                                }

                                // Notify data set changed to update all views
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

                // If this Lutemon is the selected one, update its appearance
                if (context instanceof BattleActivity) {
                    BattleActivity activity = (BattleActivity) context;
                    if (activity.isLutemonSelected(lutemon.getId())) {
                        holder.btnAction1.setText("Deselect");
                        holder.itemView.setBackgroundColor(0x33FF0000); // Light red background
                    } else {
                        holder.btnAction1.setText("Select for Battle");
                        holder.itemView.setBackgroundColor(0xFFFFFFFF); // White background
                    }
                }

                holder.btnAction2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Lutemon lutemon = lutemons.get(position);
                            lutemon.heal(); // Heal the Lutemon when returning home
                            Home.getInstance().addLutemon(lutemon);
                            BattleField.getInstance().removeLutemon(lutemon.getId());
                            lutemons.remove(position);
                            notifyItemRemoved(position);
                            checkEmpty();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lutemons.size();
    }

    private void checkEmpty() {
        if (lutemons.isEmpty()) {
            if (context instanceof HomeActivity) {
                ((HomeActivity) context).findViewById(R.id.txtNoLutemons).setVisibility(View.VISIBLE);
                ((HomeActivity) context).findViewById(R.id.recyclerView).setVisibility(View.GONE);
            } else if (context instanceof TrainingActivity) {
                ((TrainingActivity) context).findViewById(R.id.txtNoLutemons).setVisibility(View.VISIBLE);
                ((TrainingActivity) context).findViewById(R.id.recyclerView).setVisibility(View.GONE);
            } else if (context instanceof BattleActivity) {
                ((BattleActivity) context).findViewById(R.id.txtNoLutemons).setVisibility(View.VISIBLE);
                ((BattleActivity) context).findViewById(R.id.recyclerView).setVisibility(View.GONE);
            }
        }
    }

    public static class LutemonViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtStats;
        Button btnAction1, btnAction2;
        ImageView imgLutemon; // Add this

        public LutemonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtStats = itemView.findViewById(R.id.txtStats);
            btnAction1 = itemView.findViewById(R.id.btnAction1);
            btnAction2 = itemView.findViewById(R.id.btnAction2);
            imgLutemon = itemView.findViewById(R.id.imgLutemon);
        }
    }
}