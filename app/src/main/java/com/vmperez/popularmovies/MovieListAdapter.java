package com.vmperez.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vmperez.popularmovies.data.MovieInfo;
import com.vmperez.popularmovies.utilities.PopularMoviesImageUtils;

import java.net.URL;

/**
 * {@link MovieListAdapter} exposes a list of movies
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private Context mContext;
    final private MovieListAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;

    private static final int VIEW_TYPE_LANDSCAPE = 0;
    private static final int VIEW_TYPE_PORTRAIT = 1;
    private final String TAG = MovieListAdapter.class.getSimpleName();
    // private List<MovieInfo> mMovieInfoList;
    private boolean mUseLandscapeLayout;

    public MovieListAdapter(@NonNull Context context, MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mContext = context;
        // mUseLandscapeLayout = mContext.getResources().getBoolean(R.bool.use_landscape_layout);
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // mContext = viewGroup.getContext();

        int layoutIdForListItem = R.layout.movielist_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);
        MovieListAdapterViewHolder adapterViewHolder = new MovieListAdapterViewHolder(view);
        return adapterViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);

        String moviePosterPath = mCursor.getString(MainActivity.INDEX_POSTER_PATH);
        URL posterUrl = PopularMoviesImageUtils.getLargeArtForMovie(moviePosterPath);
        String posterUrlString = posterUrl.toString();

        Picasso.with(mContext).load(posterUrlString).into(movieListAdapterViewHolder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseLandscapeLayout && position == 0) {
            return VIEW_TYPE_LANDSCAPE;
        } else {
            return VIEW_TYPE_PORTRAIT;
        }
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieInfo selectedMovie);
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePoster;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // MovieInfo selectedMovie = mCursor.getExtras(adapterPosition);
            Bundle extras = mCursor.getExtras();
            byte[] blob = mCursor.getBlob(adapterPosition);
            mClickHandler.onClick(null);
        }
    }

}