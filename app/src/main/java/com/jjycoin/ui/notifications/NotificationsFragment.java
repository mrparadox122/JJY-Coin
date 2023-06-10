package com.jjycoin.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjycoin.Variables;
import com.jjycoin.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       SetViews();

        return root;
    }

    public void SetViews()
    {
        binding.accountnumber.append(Variables.accountNumber);
        binding.accountnumber.setSelected(true);
        binding.dateofbirth.append(Variables.dateOfBirth);
        binding.phonenumber.append(Variables.phoneNumber);
        binding.name.setText(Variables.Name);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}