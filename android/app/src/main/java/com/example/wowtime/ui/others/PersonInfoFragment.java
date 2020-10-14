package com.example.wowtime.ui.others;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wowtime.R;

public class PersonInfoFragment extends Fragment {
    public PersonInfoFragment(){}

    public PersonInfoFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.person_info_activity, container, false);
        ConstraintLayout constraintLayout = root.findViewById(R.id.CreditLayout);
        constraintLayout.setOnClickListener(v -> startActivity(new Intent(getActivity(), CreditDetailListActivity.class)));
        return root;
    }

}