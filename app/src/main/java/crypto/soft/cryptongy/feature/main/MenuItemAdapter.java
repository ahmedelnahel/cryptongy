package crypto.soft.cryptongy.feature.main;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import crypto.soft.cryptongy.R;

/**
 * Created by Ajahar on 11/20/2017.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {
    private Context context;
    private List<MenuItem> menuItems;
    private MenuItemClickListener menuItemClickListener;

    public MenuItemAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.icon.setImageResource(menuItem.getResourceId());
        holder.menuName.setText(menuItem.getItemName());
//        holder.menuName.setTextSize(convertPixelToSp(30));
        if (menuItem.isSelected) {
            holder.parent.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.menuName.setTextColor(Color.parseColor("#21bdb9"));
        } else {
            holder.parent.setBackgroundColor(Color.parseColor("#21bdb9"));
            holder.menuName.setTextColor(Color.parseColor("#ffffff"));
        }
        holder.parent.setOnClickListener(new OnNavItemClick(menuItem, position, holder));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public void setOnItemClickListener(MenuItemClickListener itemClickListener) {
        this.menuItemClickListener = itemClickListener;
    }

    float convertPixelToSp(int px) {
        return (px / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.menuName)
        TextView menuName;
        @BindView(R.id.parent)
        LinearLayout parent;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OnNavItemClick implements View.OnClickListener {
        MenuItem menuItem;
        int position;
        MenuItemViewHolder menuItemViewHolder;

        public OnNavItemClick(MenuItem menuItem, int position, MenuItemViewHolder menuItemViewHolder) {
            this.menuItem = menuItem;
            this.position = position;
            this.menuItemViewHolder = menuItemViewHolder;
        }

        @Override
        public void onClick(View view) {
            if (menuItemClickListener != null) {
                for (int i = 0; i < menuItems.size(); i++) {
                    menuItems.get(i).setSelected(false);
                    if (i == position) {
                        menuItems.get(i).setSelected(true);
                    }
                    switch (menuItems.get(i).getItemName()) {
                        case "Trade":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_trade_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_trade);
                            }
                            break;
                        case "Home":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_home_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_house);
                            }
                            break;
                        case "Wallet":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_wallet_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_wallet);
                            }
                            break;
                        case "Orders":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_orders_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_orders);
                            }
                            break;
                        case "Conditional":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_portfolio_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_portfolio);
                            }
                            break;
                        case "Accounts":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_accounts_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_account);
                            }
                            break;
                        case "Donate":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_bitcoin_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_bitcoin);
                            }
                            break;
                        case "About Us":
                            if (menuItems.get(i).isSelected) {
                                menuItems.get(i).setResourceId(R.drawable.ic_about_a);
                            } else {
                                menuItems.get(i).setResourceId(R.drawable.ic_about);
                            }
                            break;
                    }
                }
                notifyDataSetChanged();
                view.setBackgroundColor(Color.parseColor("#ffffff"));
                menuItemClickListener.onItemClicked(menuItem, position);
                menuItemViewHolder.menuName.setTextColor(Color.parseColor("#21bdb9"));
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
