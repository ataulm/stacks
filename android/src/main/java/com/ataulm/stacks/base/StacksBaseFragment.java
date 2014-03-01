package com.ataulm.stacks.base;

import android.app.Activity;
import android.app.Fragment;
import android.widget.Toast;

import com.novoda.notils.caster.Classes;

public class StacksBaseFragment extends Fragment {

    private StacksBaseActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(this.activity);
        this.activity = Classes.from(activity);
    }

    protected void toast(int messageResId) {
        Toast.makeText(getActivity(), messageResId, Toast.LENGTH_SHORT).show();
    }

    protected Navigator navigateTo() {
        return activity.navigateTo();
    }

}
