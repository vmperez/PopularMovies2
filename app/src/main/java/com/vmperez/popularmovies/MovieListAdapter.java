/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vmperez.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vmperez.popularmovies.data.MovieInfo;
import com.vmperez.popularmovies.utilities.PopularMoviesImageUtils;

import java.net.URL;
import java.util.List;

/**
 * {@link MovieListAdapter} exposes a list of movies
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private static final int VIEW_TYPE_LANDSCAPE = 0;
    private static final int VIEW_TYPE_PORTRAIT = 1;
    private final String TAG = MovieListAdapter.class.getSimpleName();
    final private MovieListAdapterOnClickHandler mClickHandler;
    private List<MovieInfo> mMovieInfoList;
    private boolean mUseLandscapeLayout;
    private Context mContext;


    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        // mUseLandscapeLayout = mContext.getResources().getBoolean(R.bool.use_landscape_layout);
    }

    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        mContext = viewGroup.getContext();

        int layoutIdForListItem = R.layout.movielist_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieListAdapterViewHolder adapterViewHolder = new MovieListAdapterViewHolder(view);
        return adapterViewHolder;

    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {

        MovieInfo selectedMovie = mMovieInfoList.get(position);
        String moviePosterPath = selectedMovie.getPosterPath();
        URL posterUrl = PopularMoviesImageUtils.getLargeArtForMovie(moviePosterPath);
        String posterUrlString = posterUrl.toString();
        Picasso.with(mContext).load(posterUrlString).into(movieListAdapterViewHolder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieInfoList) {
            return 0;
        }
        return mMovieInfoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseLandscapeLayout && position == 0) {
            return VIEW_TYPE_LANDSCAPE;
        } else {
            return VIEW_TYPE_PORTRAIT;
        }
    }

    public void setMovieData(List<MovieInfo> movieData) {
        mMovieInfoList = movieData;
        notifyDataSetChanged();
    }

    public interface MovieListAdapterOnClickHandler {
        void onClick(MovieInfo selectedMovie);
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePoster;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieInfo selectedMovie = mMovieInfoList.get(adapterPosition);
            mClickHandler.onClick(selectedMovie);
        }
    }

}