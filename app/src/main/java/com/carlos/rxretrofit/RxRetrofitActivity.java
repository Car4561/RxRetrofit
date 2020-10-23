package com.carlos.rxretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.carlos.rxretrofit.adapter.RepositoryAdapter;
import com.carlos.rxretrofit.api.WebService;
import com.carlos.rxretrofit.model.GitHubRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.math.MathObservable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RxRetrofitActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepositoryAdapter repositoryAdapter;
    private List<GitHubRepo> gitHubRepos;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit);
        setUpView();
        //  testObservable();
        // sinRx();
        // conRxLambda();
        //   conRxOrdenarInverso();
        //  conRxFiltrarLenguaje();
       //    conRxFiltrarLenguajeLambda();
        //  conRxFiltrarLenguajeLambdaMasOperadores();
         //conRxOrdenarPorEstrellas();
        conRxAverageEstrellas();

    }

    private void conRxAverageEstrellas() {
        Observable<Integer> observable = WebService
                .getInstance()
                .createService()
                .getReposForUserRx("Car4561")
                .toObservable()
                .flatMapIterable(e -> e)
                .map(e -> e.getStargazers_count());

        MathObservable.averageDouble(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e->Log.d("TAG1","average: " + e)
                );
    }

    private void conRxOrdenarPorEstrellas() {
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .sorted(new Comparator<GitHubRepo>() {
                            @Override
                            public int compare(GitHubRepo o1, GitHubRepo o2) {
                                return 0;
                            }
                        })
                        .observeOn(Schedulers.trampoline())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(repos->{
                                    Log.d("TAG12",Thread.currentThread().getName());
                                    gitHubRepos.add(repos);
                                    repositoryAdapter.setData(gitHubRepos);
                                },
                                t->Log.d("TAG1","error: "+t))
        );
    }

    private void conRxFiltrarLenguajeLambdaMasOperadores() {
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .filter(gitHubRepo->gitHubRepo.getLanguage().equals("Java"))
                        .observeOn(Schedulers.trampoline())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(repos->{
                                    Log.d("TAG12",Thread.currentThread().getName());

                                    gitHubRepos.add(repos);
                                    repositoryAdapter.setData(gitHubRepos);
                                },
                                t->Log.d("TAG1","error: "+t))
        );
    }

    private void conRxFiltrarLenguajeLambda() {

        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMapIterable(e->e)
                        .filter(gitHubRepo->gitHubRepo.getLanguage().equals("Java"))
                        .observeOn(Schedulers.trampoline())
                        .subscribeOn(Schedulers.computation())
                        .subscribe(repos->{
                                    Log.d("TAG12",Thread.currentThread().getName());

                                    gitHubRepos.add(repos);
                                    repositoryAdapter.setData(gitHubRepos);
                                },
                                t->Log.d("TAG1","error: "+t))
        );

    }




    private void conRxFiltrarLenguaje() {
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .toObservable()
                        .flatMap(new Function<List<GitHubRepo>, ObservableSource<GitHubRepo>>() {
                            @Override
                            public ObservableSource<GitHubRepo> apply(List<GitHubRepo> repos) throws Throwable {
                                return Observable.fromIterable(repos)  .subscribeOn(Schedulers.computation());
                            }
                        }).filter(new Predicate<GitHubRepo>() {
                                @Override
                                public boolean test(GitHubRepo gitHubRepo) throws Throwable {
                                    Log.d("TAG1",Thread.currentThread().getName());

                                    return gitHubRepo.getLanguage().equals("Java");
                                }
                         }) .observeOn(Schedulers.computation())


                        .subscribe(repos->{
                                    Log.d("TAG12",Thread.currentThread().getName());

                                    gitHubRepos.add(repos);
                            repositoryAdapter.setData(gitHubRepos);
                        },
                        t->Log.d("TAG1","error: "+t))
                    );
    }

    private void conRxOrdenarInverso() {
        compositeDisposable.add(
                WebService
                .getInstance()
                .createService()
                .getReposForUserRx("Car4561")
                .subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<List<GitHubRepo>, List<GitHubRepo>>() {
                    @Override
                    public List<GitHubRepo> apply(List<GitHubRepo> repos) throws Throwable {
                        Collections.sort(repos, new Comparator<GitHubRepo>() {
                            @Override
                            public int compare(GitHubRepo o1, GitHubRepo o2) {
                                return o2.getName().compareTo(o1.getName());
                            }

                        });
                        return  repos;
                    }
                })
                        .subscribe(repos->repositoryAdapter.setData(repos),
                                t->Log.d("TAG1","error: "+t))

        );
    }


    private  void conRx(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<GitHubRepo>>() {
                                       @Override
                                       public void accept(List<GitHubRepo> repos) throws Throwable {
                                           repositoryAdapter.setData(repos);
                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable t) throws Throwable {
                                           Log.d("TAG1","Error " + t.getMessage());

                                       }
                                   }
                        )
        );
    }

    private  void conRxLambda(){
        compositeDisposable.add(
                WebService
                        .getInstance()
                        .createService()
                        .getReposForUserRx("Car4561")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(repos -> repositoryAdapter.setData(repos),
                                t -> Log.d("TAG1","Error " + t.getMessage())
                        )
        );
    }

    private  void sinRx(){
        Call<List<GitHubRepo>> call = WebService.getInstance().createService().getReposForUser("Car4561");
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {
                 gitHubRepos = response.body();
                 repositoryAdapter.setData(gitHubRepos);
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                Log.d("TAG1","Error " + t.getMessage());
            }
        });
    }


    private void setUpView() {
        compositeDisposable = new CompositeDisposable();
        gitHubRepos = new ArrayList<>();
        repositoryAdapter = new RepositoryAdapter(gitHubRepos);
        recyclerView  = findViewById(R.id.recyclerView);
        LinearLayoutManager ly = new LinearLayoutManager(getApplicationContext());
        ly.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(ly);
        recyclerView.setAdapter(repositoryAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void testObservable() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                Log.d("TAG1",Thread.currentThread().getName());
                emitter.onNext("Carlos");
                emitter.onNext("Llerena");
                emitter.onNext("Llerena");
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Throwable {
                Log.d("TAG1",Thread.currentThread().getName());

                return Observable.just(s);
            }
        })
                .map(new Function<String, String >() {
                    @Override
                    public String apply(String s) throws Throwable {
                        Log.d("TAG1",Thread.currentThread().getName());

                        return s;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {

                               }

                               @Override
                               public void onNext(@NonNull String s) {
                                   Log.d("TAG1",s);
                               }

                               @Override
                               public void onError(@NonNull Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}