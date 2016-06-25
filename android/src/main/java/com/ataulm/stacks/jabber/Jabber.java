package com.ataulm.stacks.jabber;

import android.content.Context;

import com.ataulm.stacks.SharedPreferencesJsonRepository;
import com.ataulm.stacks.navigation.UriCreator;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.JsonRepository;
import com.ataulm.stacks.stack.JsonStack;
import com.ataulm.stacks.stack.JsonStackConverter;
import com.ataulm.stacks.stack.JsonStacksRepository;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.StacksRepository;
import com.ataulm.stacks.stack.UpdateStackUsecase;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.util.List;

public final class Jabber {

    private static Jabber instance;

    private final Context context;

    private Usecases usecases;
    private StacksRepository stacksRepository;
    private UriResolver uriResolver;
    private UriCreator uriCreator;

    public static Jabber init(Context context) {
        if (instance == null) {
            instance = new Jabber(context.getApplicationContext());
        }
        return instance;
    }

    private Jabber(Context context) {
        this.context = context;
    }

    public static void toast(String message) {
        Toaster.displayToast(instance.context, message);
    }

    public static UriCreator uriCreator() {
        if (instance.uriCreator == null) {
            instance.uriCreator = UriCreator.create(instance.context);
        }
        return instance.uriCreator;
    }

    public static UriResolver uriResolver() {
        if (instance.uriResolver == null) {
            instance.uriResolver = UriResolver.create(instance.context);
        }
        return instance.uriResolver;
    }

    public static FetchStacksUsecase fetchStacksUsecase() {
        return usecases().fetchStacks();
    }

    public static CreateStackUsecase createStacksUsecase() {
        return usecases().createStacks();
    }

    public static PersistStacksUsecase persistStacksUsecase() {
        return usecases().persistStacks();
    }

    public static UpdateStackUsecase updateStacksUsecase() {
        return usecases().updateStacks();
    }

    public static RemoveStackUsecase removeStackUsecase() {
        return usecases().removeStacks();
    }

    public static Usecases usecases() {
        if (instance.usecases == null) {
            instance.usecases = Usecases.create(stacksRepository());
        }
        return instance.usecases;
    }

    private static StacksRepository stacksRepository() {
        if (instance.stacksRepository == null) {
            instance.stacksRepository = createStacksRepository(instance.context);
        }
        return instance.stacksRepository;
    }

    private static StacksRepository createStacksRepository(Context context) {
        Context applicationContext = context.getApplicationContext();
        JsonRepository jsonRepository = SharedPreferencesJsonRepository.create(applicationContext);
        JsonAdapter<List<JsonStack>> adapter = new Moshi.Builder().build().adapter(Types.newParameterizedType(List.class, JsonStack.class));
        JsonStacksRepository jsonStacksRepository = new JsonStacksRepository(
                jsonRepository,
                adapter
        );
        return new StacksRepository(jsonStacksRepository, new JsonStackConverter());
    }

}
