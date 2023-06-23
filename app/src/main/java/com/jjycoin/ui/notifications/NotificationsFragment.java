package com.jjycoin.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jjycoin.Login;
import com.jjycoin.Variables;
import com.jjycoin.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    @Nullable
    private FragmentNotificationsBinding binding;

    SharedPreferences sharedPreferences;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       SetViews();
       binding.logout.setOnClickListener(v -> {
           sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
           SharedPreferences.Editor editor = sharedPreferences.edit();
           editor.putString("username", "");
           editor.putString("password", "");
           editor.apply();
           requireActivity().startActivity(new Intent(requireContext(), Login.class));
           requireActivity().finish();
       });
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