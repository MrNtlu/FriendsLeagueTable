package com.mrntlu.friendsleaguetable.adapters.leaguedetailsactivity.leaguehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.mrntlu.friendsleaguetable.R;
import com.mrntlu.friendsleaguetable.callbacks.leaguedetailsactivity.LeagueHubPagerOnClick;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LeagueHubPagerAdapter extends PagerAdapter {

    private List<ViewPagerModel> viewPagerModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private LeagueHubPagerOnClick pagerOnClick;

    @Inject
    public LeagueHubPagerAdapter(Context context) {
        this.viewPagerModels = new ArrayList<ViewPagerModel>(){{
            add(new ViewPagerModel("Results","Results of the past matches.",R.drawable.ic_pvp));
            add(new ViewPagerModel("Standings","League standings.",R.drawable.ic_rank));
            add(new ViewPagerModel("Details","Change or view league details.",R.drawable.ic_panel));
        }};
        this.context = context;
    }

    @Override
    public int getCount() {
        return viewPagerModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.cell_league_hub,container,false);

        ImageView imageView;
        TextView title,desc;

        imageView=view.findViewById(R.id.image);
        title=view.findViewById(R.id.title);
        desc=view.findViewById(R.id.desc);

        imageView.setImageResource(viewPagerModels.get(position).getImage());
        title.setText(viewPagerModels.get(position).getTitle());
        desc.setText(viewPagerModels.get(position).getDesc());

        view.setOnClickListener((view1 -> {
            pagerOnClick.onViewPagerClick(position);
        }));

        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public List<ViewPagerModel> getViewPagerModels() {
        return viewPagerModels;
    }

    public void setPagerOnClick(LeagueHubPagerOnClick pagerOnClick) {
        this.pagerOnClick = pagerOnClick;
    }

    public class ViewPagerModel{
        private String title;
        private String desc;
        private int image;

        public ViewPagerModel(String title, String desc, int image) {
            this.title = title;
            this.desc = desc;
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "ViewPagerModel{" +
                    "title='" + title + '\'' +
                    ", desc='" + desc + '\'' +
                    ", image=" + image +
                    '}';
        }
    }
}
