package reale.francesco.com.popularmovie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;

import reale.francesco.com.popularmovie.Utils.NetworkUtils;
import reale.francesco.com.popularmovie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListner, LoaderManager.LoaderCallbacks<ListOfMovies> {

    ActivityMainBinding bind;
    private Toast toast = null;
    private static final int MOVIE_LOADER = 22;
    private int actualItem = 0 ;
    private boolean isPopular = true;
    private boolean oldIsPopular ;
    private int actualPage = 1 ;
    private static final String LAST_PAGE_SAVED =  "LASTPAGE";
    private static final String LAST_LIST =  "LIST_OF_MOVIE";
    private static final String POPULAR = "POPULAR";
    private static final String OLD_POPULAR = "OLD_POPULAR";
    private static final String PAGE = "PAGE";
    private Spinner spinner = null;
    private  ArrayAdapter<CharSequence> spinnerAdapter = null;
    ListOfMovies list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldIsPopular = isPopular;
        bind = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(bind.toolbar);

        GridLayoutManager gridManager = (GridLayoutManager)
                bind.recyclerViewMovies.getLayoutManager();

        bind.recyclerViewMovies.setLayoutManager(gridManager);
        bind.recyclerViewMovies.setHasFixedSize(true);

        final MovieAdapter adapter = new MovieAdapter(this);
        bind.recyclerViewMovies.setAdapter(adapter);
        bind.recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // each time the recycle view is scrolled we need to check if we should perform a query
                int totalOfItems = bind.recyclerViewMovies.getAdapter().getItemCount();
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();
                actualItem = layoutManager.findFirstVisibleItemPosition();
                if(layoutManager.findLastCompletelyVisibleItemPosition() == totalOfItems -1) {
                    if(list.getTotalPagesNum()>actualPage) { // maybe we have finished the total pages to display
                        incrementPage(); // increment number of page
                        sendQueryToDb(); // send the query to the movieDbAPI
                    }
                }
            }
        });

        if(restoreInstance(savedInstanceState)){            // check if there is something to restore
            updateMovies();                                 // update movies with data restored
            bind.recyclerViewMovies.scrollToPosition(actualItem);   // scroll to the last Item displayed in the old session
        }
    }


    public int getActualPage() {
        return actualPage;
    }

    public int incrementPage() {
        return actualPage++;
    }

    public void setActualPage(int actualPage) {
        this.actualPage = actualPage;
    }

    void sendQueryToDb(){
        if (NetworkUtils.isOnline(this)) {      // check if the device is Online
            getSupportLoaderManager()
                    .restartLoader(MOVIE_LOADER, null, MainActivity.this)
                    .forceLoad();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("")
                    .setTitle(R.string.internet_error);

            builder.setPositiveButton(R.string.retry_connection, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    sendQueryToDb();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem spinnerMenuItem = menu.findItem(R.id.spinner);
        spinner = (Spinner)spinnerMenuItem.getActionView();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView)view).setText(null);
                if(position == 0 ){
                    oldIsPopular = isPopular;

                    bind.toolbar.setTitle(R.string.PopularMovie);
                    if(list == null || isPopular == false){
                        setActualPage(1);
                        sendQueryToDb();
                    }
                    isPopular = true;
                }else if(position == 1){
                    oldIsPopular = isPopular;
                    bind.toolbar.setTitle(R.string.TopRated);
                    if(list == null || isPopular == true){
                        setActualPage(1);
                        sendQueryToDb();
                    }
                    isPopular = false;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerAdapter = ArrayAdapter.
                createFromResource(this, R.array.order_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        if(isPopular){
            int spinnerPosition = spinnerAdapter.getPosition("Popular Movie");
            spinner.setSelection(spinnerPosition);
        }else{
            int spinnerPosition = spinnerAdapter.getPosition("Top Rated");
            spinner.setSelection(spinnerPosition);
        }
        return true;
    }


    protected boolean restoreInstance(Bundle savedInst) {

        if (savedInst == null) return false;
        if (!savedInst.containsKey(LAST_PAGE_SAVED)) return false;
        if (!savedInst.containsKey(LAST_LIST)) return false;
        if (!savedInst.containsKey(POPULAR)) return false;
        if (!savedInst.containsKey(OLD_POPULAR)) return false;

        isPopular = savedInst.getBoolean(POPULAR);
        oldIsPopular = savedInst.getBoolean(OLD_POPULAR);

        list = savedInst.getParcelable(LAST_LIST);

        actualItem = savedInst.getInt(LAST_PAGE_SAVED);

        actualPage = savedInst.getInt(PAGE);

        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

            state.putBoolean(POPULAR, isPopular);
            state.putBoolean(OLD_POPULAR, oldIsPopular);
            state.putParcelable(LAST_LIST, list);
            state.putInt(PAGE, actualPage);
            state.putInt(LAST_PAGE_SAVED,actualItem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.spinner) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGridMovieClick(Movie clickedFilm) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE, clickedFilm);
        startActivity(intent);
    }


    @NonNull
    @Override
    public Loader<ListOfMovies> onCreateLoader(int id, @Nullable Bundle args) {

       // bind.indeterminateBar.setVisibility(View.VISIBLE);
        return new AsyncTaskLoader<ListOfMovies>(this) {

            @Override
            public ListOfMovies loadInBackground() {

                try {
                    URL url = NetworkUtils.buildUrl(isPopular,getActualPage());
                    if (url == null)
                        return null;

                    String response = NetworkUtils.getResponseFromHttpUrl(url);
                    if (response != null) {
                        Type type = new TypeToken<ListOfMovies>(){}.getType();
                        return new Gson().fromJson(response, type);
                    }
                    else
                        return null;
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ListOfMovies> loader, ListOfMovies data) {

        ListOfMovies listFinished =  data;
        if(oldIsPopular == isPopular) {
            if (list != null && list.getResults().size() > 0) {
                list.setPage(listFinished.getPage());
                list.getResults().addAll(data.getResults());
            } else // this is the first time i'm loading
                list = listFinished;
        }else{
            bind.recyclerViewMovies.scrollToPosition(0);
            list = listFinished;
            oldIsPopular = isPopular;
        }
        updateMovies();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ListOfMovies> loader) {

    }


    private void updateMovies() {
        if (list != null) {
            MovieAdapter adapter = (MovieAdapter) bind.recyclerViewMovies.getAdapter();
            adapter.setList(list.getResults());
            adapter.notifyDataSetChanged();
        }
    }


}
