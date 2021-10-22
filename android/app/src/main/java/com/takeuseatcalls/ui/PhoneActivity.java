package com.takeuseatcalls.ui;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.takeuseatcalls.R;
import com.takeuseatcalls.databinding.ActivityPhoneBinding;
import com.takeuseatcalls.model.CallState;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PhoneActivity extends AppCompatActivity {

    private ActivityPhoneBinding binding;

    private PhoneViewModel viewModel;

    @Inject
    public KeyguardManager keyguardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PhoneViewModel.class);

        setupBindings();
        setupObservers();

        addLockScreenFlags();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onCallEnd();
    }

    private void addLockScreenFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupObservers() {
        viewModel.dialpadString.observe(this, str -> {
            binding.dialpad.tvDialpad.setText(str);
        });

        viewModel.callState.observe(this, state -> {
            if (state == CallState.INCOMING) {
                binding.cntrIncomingCall.setVisibility(View.VISIBLE);
                binding.cntrCall.setVisibility(View.INVISIBLE);
            } else if (state == CallState.DISCONNECTED) {
                finishAffinity();
            } else {
                binding.cntrIncomingCall.setVisibility(View.INVISIBLE);
                binding.cntrCall.setVisibility(View.VISIBLE);
            }

            binding.tvStateTitle.setText(state.getStringRes());
        });

        viewModel.callContact.observe(this, contact -> {


            binding.tvUserNumber.setText(contact.getPhone());

            String title = contact.getFirstName() + contact.getLastName();

            if (title.trim().isEmpty())
                binding.tvUserTitle.setText(R.string.unknown);
            else
                binding.tvUserTitle.setText(title);

            Glide.with(this)
                    .load(contact.getPhotoUrl())
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.imgUserAvatar);
        });

        viewModel.isMicrophoneMute.observe(this, it -> {
            if (it) {
                binding.callToggleMicrophone.setImageResource(R.drawable.ic_microphone_off_vector);
            } else {
                binding.callToggleMicrophone.setImageResource(R.drawable.ic_microphone_vector);
            }
        });

        viewModel.isSpeakerOn.observe(this, it -> {
            if (it)
                binding.btnCallToggleSpeaker.setImageResource(R.drawable.ic_speaker_on_vector);
            else
                binding.btnCallToggleSpeaker.setImageResource(R.drawable.ic_speaker_off_vector);
        });

        viewModel.isDialupShown.observe(this, it -> {
            if (it) {
                binding.cntrDialpad.setVisibility(View.VISIBLE);
            } else {
                binding.cntrDialpad.setVisibility(View.GONE);
            }
        });
    }

    private void setupBindings() {
        binding.dialpad.dialpad0Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('0');
        });
        binding.dialpad.dialpad1Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('1');
        });
        binding.dialpad.dialpad2Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('2');
        });
        binding.dialpad.dialpad3Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('3');
        });
        binding.dialpad.dialpad4Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('4');
        });
        binding.dialpad.dialpad5Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('5');
        });
        binding.dialpad.dialpad6Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('6');
        });
        binding.dialpad.dialpad7Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('7');
        });
        binding.dialpad.dialpad8Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('8');
        });
        binding.dialpad.dialpad9Holder.setOnClickListener(v -> {
            viewModel.onDialpadPress('9');
        });
        binding.dialpad.dialpadHashtagHolder.setOnClickListener(v -> {
            viewModel.onDialpadPress('#');
        });
        binding.dialpad.dialpadAsteriskHolder.setOnClickListener(v -> {
            viewModel.onDialpadPress('*');
        });

        binding.btnCallAccept.setOnClickListener(v -> {
            viewModel.onCallStart();
        });

        binding.btnCallDecline.setOnClickListener(v -> {
            viewModel.onCallEnd();
        });

        binding.btnCallEnd.setOnClickListener(v -> {
            viewModel.onCallEnd();
        });

        binding.btnCallToggleSpeaker.setOnClickListener(v -> {
            viewModel.onSpeakerToggleSpeaker();
        });

        binding.callToggleMicrophone.setOnClickListener(v -> {
            viewModel.onMicrophoneToggle();
        });

        binding.btnCallDialpad.setOnClickListener(v -> {
            viewModel.onDialupToggle();
        });
    }
}