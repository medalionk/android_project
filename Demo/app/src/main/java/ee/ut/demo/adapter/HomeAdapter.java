package ee.ut.demo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ee.ut.demo.R;
import ee.ut.demo.helpers.Parse;
import ee.ut.demo.mvp.model.Article;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class HomeAdapter  extends RecyclerView.Adapter<HomeViewHolder> {

    private Context mContext;
    private List<Article> mArticles;

    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
        this.mArticles = new ArrayList<>();
    }

    public void addAll(Collection<Article> articles) {
        this.mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void add(Article article) {
        this.mArticles.add(article);
        notifyDataSetChanged();
    }

    public void replaceArticles(Collection<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        Collections.sort(mArticles, new Comparator<Article>() {
            @Override
            public int compare(Article a, Article b) {
                return b.getId().compareToIgnoreCase(a.getId());
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card, parent, false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        final Article article = mArticles.get(position);
        holder.title.setText(article.getTitle());
        holder.except.setText(article.getExcerpt());

        holder.publicUrl.setMovementMethod(LinkMovementMethod.getInstance());
        holder.publicUrl.setText(Parse.fromHtml("<a href=\""+ article.getPublicUrl() + "\">"
                + "More Info" + "</a>"));
        holder.publicUrl.setClickable(true);

        if(article.getImageUrl() == null){
            Picasso.with(mContext)
                    .load(R.drawable.pic1)
                    .into(holder.thumbnail);
        }else {
            Picasso.with(mContext)
                    .load(Uri.parse(Parse.imageUrl(article.getImageUrl())))
                    .into(holder.thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
