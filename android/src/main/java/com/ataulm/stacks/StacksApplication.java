package com.ataulm.stacks;

import android.app.Application;

import com.ataulm.stacks.stack.AsyncFetchStacksUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.JsonRepository;
import com.ataulm.stacks.stack.JsonStack;
import com.ataulm.stacks.stack.JsonStackConverter;
import com.ataulm.stacks.stack.JsonStacksRepository;
import com.ataulm.stacks.stack.StacksRepository;
import com.ataulm.stacks.stack.SyncFetchStacksUsecase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.List;

public class StacksApplication extends Application {

    private static JsonStacksRepository jsonStacksRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        JsonRepository jsonRepository = SharedPreferencesJsonRepository.create(this);
        JsonAdapter<List<JsonStack>> adapter = new Moshi.Builder().build().adapter(Types.newParameterizedType(List.class, JsonStack.class));
        jsonStacksRepository = new JsonStacksRepository(
                jsonRepository,
                adapter
        );
    }

    public static FetchStacksUsecase createFetchStacksUsecase() {
        return new AsyncFetchStacksUsecase(new SyncFetchStacksUsecase(new StacksRepository(jsonStacksRepository, new JsonStackConverter())));
    }

}
