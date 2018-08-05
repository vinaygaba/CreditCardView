/*
 * Copyright (C) 2015 Vinay Gaba
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

package com.vinaygaba.creditcardview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinaygaba.creditcardview.util.AndroidUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Pattern;

import static com.vinaygaba.creditcardview.CardNumberFormat.ALL_DIGITS;
import static com.vinaygaba.creditcardview.CardNumberFormat.MASKED_ALL;
import static com.vinaygaba.creditcardview.CardNumberFormat.MASKED_ALL_BUT_LAST_FOUR;
import static com.vinaygaba.creditcardview.CardNumberFormat.ONLY_LAST_FOUR;
import static com.vinaygaba.creditcardview.CardType.AMERICAN_EXPRESS;
import static com.vinaygaba.creditcardview.CardType.AUTO;
import static com.vinaygaba.creditcardview.CardType.DISCOVER;
import static com.vinaygaba.creditcardview.CardType.MASTERCARD;
import static com.vinaygaba.creditcardview.CardType.PATTERN_AMERICAN_EXPRESS;
import static com.vinaygaba.creditcardview.CardType.PATTERN_DISCOVER;
import static com.vinaygaba.creditcardview.CardType.PATTERN_MASTER_CARD;
import static com.vinaygaba.creditcardview.CardType.VISA;

@SuppressLint("DefaultLocale")
public class CreditCardView extends RelativeLayout {

    private static final int CARD_FRONT = 0;
    private static final int CARD_BACK = 1;
    private static final boolean DEBUG = false;
    private final Context mContext;
    private String mCardNumber;
    private String mCardName;
    private String mExpiryDate;
    private String mCvv;
    private String mFontPath;
    private int mCardNumberTextColor = Color.WHITE;
    private int mCardNumberFormat = ALL_DIGITS;
    private int mCardNameTextColor = Color.WHITE;
    private int mExpiryDateTextColor = Color.WHITE;
    private int mCvvTextColor = Color.BLACK;
    private int mValidTillTextColor = Color.WHITE;
    @CreditCardType
    private int mType = VISA;
    private int mBrandLogo;
    private int cardSide = CARD_FRONT;
    private boolean mPutChip;
    private boolean mIsEditable;
    private boolean mIsCardNumberEditable;
    private boolean mIsCardNameEditable;
    private boolean mIsExpiryDateEditable;
    private boolean mIsCvvEditable;
    private boolean mIsFlippable;
    private int mHintTextColor = Color.WHITE;
    private int mCvvHintColor = Color.WHITE;
    @DrawableRes private int mCardFrontBackground;
    @DrawableRes private int mCardBackBackground;
    private Typeface mCreditCardTypeFace;
    private ImageButton mFlipBtn;
    private EditText mCardNumberView;
    private EditText mCardNameView;
    private EditText mExpiryDateView;
    private EditText mCvvView;
    private ImageView mCardTypeView;
    private ImageView mBrandLogoView;
    private ImageView mChipView;
    private TextView mValidTill;
    private View mStripe;
    private View mAuthorizedSig;
    private View mSignature;

    public CreditCardView(Context context) {
        this(context, null);
    }

    public CreditCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (context != null) {
            this.mContext = context;
        } else {
            this.mContext = getContext();
        }

        init();
        loadAttributes(attrs);
        initDefaults();
        addListeners();
    }

    /**
     * Initialize various views and variables
     */
    private void init() {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.creditcardview, this, true);

        mCardNumberView = (EditText) findViewById(R.id.card_number);
        mCardNameView = (EditText) findViewById(R.id.card_name);
        mCardTypeView = (ImageView) findViewById(R.id.card_logo);
        mBrandLogoView = (ImageView) findViewById(R.id.brand_logo);
        mChipView = (ImageView) findViewById(R.id.chip);
        mValidTill = (TextView) findViewById(R.id.valid_till);
        mExpiryDateView = (EditText) findViewById(R.id.expiry_date);
        mFlipBtn = (ImageButton) findViewById(R.id.flip_btn);
        mCvvView = (EditText) findViewById(R.id.cvv_et);
        mStripe = findViewById(R.id.stripe);
        mAuthorizedSig = findViewById(R.id.authorized_sig_tv);
        mSignature = findViewById(R.id.signature);
    }

    private void loadAttributes(@Nullable AttributeSet attrs) {

        final TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CreditCardView, 0, 0);

        try {
            mCardNumber = a.getString(R.styleable.CreditCardView_cardNumber);
            mCardName = a.getString(R.styleable.CreditCardView_cardName);
            mExpiryDate = a.getString(R.styleable.CreditCardView_expiryDate);
            mCardNumberTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor,
                    Color.WHITE);
            mCardNumberFormat = a.getInt(R.styleable.CreditCardView_cardNumberFormat, 0);
            mCardNameTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor,
                    Color.WHITE);
            mExpiryDateTextColor = a.getColor(R.styleable.CreditCardView_expiryDateTextColor,
                    Color.WHITE);
            mCvvTextColor = a.getColor(R.styleable.CreditCardView_cvvTextColor,
                    Color.BLACK);
            mValidTillTextColor = a.getColor(R.styleable.CreditCardView_validTillTextColor,
                    Color.WHITE);
            //noinspection WrongConstant
            mType = a.getInt(R.styleable.CreditCardView_type, VISA);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView_brandLogo, 0);
            // mBrandLogoPosition = a.getInt(R.styleable.CreditCardView_brandLogoPosition, 1);
            mPutChip = a.getBoolean(R.styleable.CreditCardView_putChip, false);
            mIsEditable = a.getBoolean(R.styleable.CreditCardView_isEditable, false);
            //For more granular control to the fields. Issue #7
            mIsCardNameEditable = a.getBoolean(R.styleable.CreditCardView_isCardNameEditable,
                    mIsEditable);
            mIsCardNumberEditable = a.getBoolean(R.styleable.CreditCardView_isCardNumberEditable,
                    mIsEditable);
            mIsExpiryDateEditable = a.getBoolean(R.styleable.CreditCardView_isExpiryDateEditable,
                    mIsEditable);
            mIsCvvEditable = a.getBoolean(R.styleable.CreditCardView_isCvvEditable, mIsEditable);
            mHintTextColor = a.getColor(R.styleable.CreditCardView_hintTextColor, Color.WHITE);
            mIsFlippable = a.getBoolean(R.styleable.CreditCardView_isFlippable, mIsFlippable);
            mCvv = a.getString(R.styleable.CreditCardView_cvv);
            mCardFrontBackground = a.getResourceId(R.styleable.CreditCardView_cardFrontBackground,
                    R.drawable.cardbackground_sky);
            mCardBackBackground = a.getResourceId(R.styleable.CreditCardView_cardBackBackground,
                    R.drawable.cardbackground_canvas);
            mFontPath = a.getString(R.styleable.CreditCardView_fontPath);

        } finally {
            a.recycle();
        }
    }

    private void initDefaults() {

        setBackgroundResource(mCardFrontBackground);

        if (TextUtils.isEmpty(mFontPath)) {
            // Default Font path
            mFontPath = mContext.getString(R.string.font_path);
        }

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            // Loading Font Face
            mCreditCardTypeFace = Typeface.createFromAsset(mContext.getAssets(), mFontPath);
        }

        if (TextUtils.isEmpty(mFontPath)) {
            // Default Font path
            mFontPath = mContext.getString(R.string.font_path);
        }

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            // Loading Font Face
            mCreditCardTypeFace = Typeface.createFromAsset(mContext.getAssets(), mFontPath);
        }

        if (!mIsEditable) {
            // If card is not set to be editable, disable the edit texts
            mCardNumberView.setEnabled(false);
            mCardNameView.setEnabled(false);
            mExpiryDateView.setEnabled(false);
            mCvvView.setEnabled(false);
        } else {
            // If the card is editable, set the hint text and hint values which will be displayed
            // when the edit text is blank
            mCardNumberView.setHint(R.string.card_number_hint);
            mCardNumberView.setHintTextColor(mHintTextColor);

            mCardNameView.setHint(R.string.card_name_hint);
            mCardNameView.setHintTextColor(mHintTextColor);

            mExpiryDateView.setHint(R.string.expiry_date_hint);
            mExpiryDateView.setHintTextColor(mHintTextColor);

            mCvvView.setHint(R.string.cvv_hint);
            mCvvView.setHintTextColor(mCvvTextColor);
        }

        //For more granular control of the editable fields. Issue #7
        if (mIsCardNameEditable != mIsEditable) {
            //If the mIsCardNameEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if (mIsCardNameEditable) {
                mCardNameView.setHint(R.string.card_name_hint);
                mCardNameView.setHintTextColor(mHintTextColor);
            } else {
                mCardNameView.setHint("");
            }

            mCardNameView.setEnabled(mIsCardNameEditable);
        }

        if (mIsCardNumberEditable != mIsEditable) {
            //If the mIsCardNumberEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if (mIsCardNumberEditable) {
                mCardNumberView.setHint(R.string.card_number_hint);
                mCardNumberView.setHintTextColor(mHintTextColor);
            } else {
                mCardNumberView.setHint("");
            }
            mCardNumberView.setEnabled(mIsCardNumberEditable);
        }

        if (mIsExpiryDateEditable != mIsEditable) {
            //If the mIsExpiryDateEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if (mIsExpiryDateEditable) {
                mExpiryDateView.setHint(R.string.expiry_date_hint);
                mExpiryDateView.setHintTextColor(mHintTextColor);
            } else {
                mExpiryDateView.setHint("");
            }
            mExpiryDateView.setEnabled(mIsExpiryDateEditable);
        }

        // If card number is not null, add space every 4 characters and format it in the appropriate
        // format
        if (!TextUtils.isEmpty(mCardNumber)) {
            mCardNumberView.setText(getFormattedCardNumber(addSpaceToCardNumber()));
        }

        // Set the user entered card number color to card number field
        mCardNumberView.setTextColor(mCardNumberTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            mCardNumberView.setTypeface(mCreditCardTypeFace);
        }

        // If card name is not null, convert the text to upper case
        if (!TextUtils.isEmpty(mCardName)) {
            mCardNameView.setText(mCardName.toUpperCase());
        }

        // This filter will ensure the text entered is in uppercase when the user manually enters
        // the card name
        mCardNameView.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });

        // Set the user entered card name color to card name field
        mCardNameView.setTextColor(mCardNumberTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            mCardNameView.setTypeface(mCreditCardTypeFace);
        }

        // Set the appropriate logo based on the type of card
        mCardTypeView.setBackgroundResource(getLogo());

        // If background logo attribute is present, set it as the brand logo background resource
        if (mBrandLogo != 0) {
            mBrandLogoView.setBackgroundResource(mBrandLogo);
            // brandLogo.setLayoutParams(params);
        }

        // If putChip attribute is present, change the visibility of the putChip view and display it
        if (mPutChip) {
            mChipView.setVisibility(View.VISIBLE);
        }

        // If expiry date is not null, set it to the expiryDate TextView
        if (!TextUtils.isEmpty(mExpiryDate)) {
            mExpiryDateView.setText(mExpiryDate);
        }

        // Set the user entered expiry date color to expiry date field
        mExpiryDateView.setTextColor(mExpiryDateTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            mExpiryDateView.setTypeface(mCreditCardTypeFace);
        }

        // Set the appropriate text color to the validTill TextView
        mValidTill.setTextColor(mValidTillTextColor);

        // If CVV is not null, set it to the expiryDate TextView
        if (!TextUtils.isEmpty(mCvv)) {
            mCvvView.setText(mCvv);
        }

        // Set the user entered card number color to card number field
        mCvvView.setTextColor(mCvvTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            mCvvView.setTypeface(mCreditCardTypeFace);
        }

        if (mIsCvvEditable != mIsEditable) {

            if (mIsCvvEditable) {
                mCvvView.setHint(R.string.cvv_hint);
                mCvvView.setHintTextColor(mCvvHintColor);
            } else {
                mCvvView.setHint("");
            }

            mCvvView.setEnabled(mIsCvvEditable);

        }

        if (mIsFlippable) {
            mFlipBtn.setVisibility(View.VISIBLE);
        }
        mFlipBtn.setEnabled(mIsFlippable);

    }

    private void addListeners() {

        // Add text change listener
        mCardNumberView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Change card type to auto to dynamically detect the card type based on the card
                // number
                mType = AUTO; //TODO - is this really required?
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Delete any spaces the user might have entered manually. The library automatically
                // adds spaces after every 4 characters to the view.
                mCardNumber = s.toString().replaceAll("\\s+", "");
            }
        });

        // Add focus change listener to detect focus being shifted from the cardNumber EditText
        mCardNumberView.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If the field just lost focus
                if (!hasFocus) {
                    //Fix for NPE. Issue #6
                    if (!TextUtils.isEmpty(mCardNumber) && mCardNumber.length() > 12) {
                        // If card type is "auto",find the appropriate logo
                        if (mType == AUTO) {
                            mCardTypeView.setBackgroundResource(getLogo());
                        }

                        // If the length of card is >12, add space every 4 characters and format it
                        // in the appropriate format
                        mCardNumberView.setText(getFormattedCardNumber(addSpaceToCardNumber()));
                    }
                }
            }
        });

        mCardNameView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Set the mCardName attribute the user entered value in the Card Name field
                mCardName = s.toString().toUpperCase();
            }
        });

        mExpiryDateView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Set the mExpiryDate attribute the user entered value in the Expiry Date field
                mExpiryDate = s.toString();
            }
        });

        mFlipBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flip();
            }
        });
    }

    public boolean isFlippable() {
        return mIsFlippable;
    }

    public void setIsFlippable(boolean flippable) {
        mIsFlippable = flippable;
        mFlipBtn.setVisibility(mIsFlippable ? View.VISIBLE : View.INVISIBLE);
        mFlipBtn.setEnabled(mIsFlippable);
    }

    public void flip() {
        if (mIsFlippable) {
            if (AndroidUtils.icsOrBetter()) {
                if (cardSide == CARD_FRONT) {
                    rotateInToBack();
                } else if (cardSide == CARD_BACK) {
                    rotateInToFront();
                }
            } else {
                if (cardSide == CARD_FRONT) {
                    rotateInToBackBeforeEleven();
                } else if (cardSide == CARD_BACK) {
                    rotateInToFrontBeforeEleven();
                }
            }

        }
    }

    private void showFrontView() {
        mCardNumberView.setVisibility(View.VISIBLE);
        mCardNameView.setVisibility(View.VISIBLE);
        mCardTypeView.setVisibility(View.VISIBLE);
        mBrandLogoView.setVisibility(View.VISIBLE);
        if (mPutChip) {
            mChipView.setVisibility(View.VISIBLE);
        }
        mValidTill.setVisibility(View.VISIBLE);
        mExpiryDateView.setVisibility(View.VISIBLE);
    }

    private void hideFrontView() {
        mCardNumberView.setVisibility(View.GONE);
        mCardNameView.setVisibility(View.GONE);
        mCardTypeView.setVisibility(View.GONE);
        mBrandLogoView.setVisibility(View.GONE);
        mChipView.setVisibility(View.GONE);
        mValidTill.setVisibility(View.GONE);
        mExpiryDateView.setVisibility(View.GONE);
    }

    private void showBackView() {
        mStripe.setVisibility(View.VISIBLE);
        mAuthorizedSig.setVisibility(View.VISIBLE);
        mSignature.setVisibility(View.VISIBLE);
        mCvvView.setVisibility(View.VISIBLE);
    }

    private void hideBackView() {
        mStripe.setVisibility(View.GONE);
        mAuthorizedSig.setVisibility(View.GONE);
        mSignature.setVisibility(View.GONE);
        mCvvView.setVisibility(View.GONE);
    }

    private void redrawViews() {
        invalidate();
        requestLayout();
    }

    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if (cardNumber == null) {
            throw new NullPointerException("Card Number cannot be null.");
        }
        this.mCardNumber = cardNumber.replaceAll("\\s+", "");
        this.mCardNumberView.setText(addSpaceToCardNumber());
        redrawViews();
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        if (cardName == null) {
            throw new NullPointerException("Card Name cannot be null.");
        }
        this.mCardName = cardName.toUpperCase();
        this.mCardNameView.setText(mCardName);
        redrawViews();
    }

    @ColorInt
    public int getCardNumberTextColor() {
        return mCardNumberTextColor;
    }

    public void setCardNumberTextColor(@ColorInt int cardNumberTextColor) {
        this.mCardNumberTextColor = cardNumberTextColor;
        this.mCardNumberView.setTextColor(mCardNumberTextColor);
        redrawViews();
    }

    @CreditCardFormat
    public int getCardNumberFormat() {
        return mCardNumberFormat;
    }

    public void setCardNumberFormat(@CreditCardFormat int cardNumberFormat) {
        if (cardNumberFormat < 0 | cardNumberFormat > 3) {
            throw new UnsupportedOperationException("CardNumberFormat: " + cardNumberFormat + "  " +
                    "is not supported. Use `CardNumberFormat.*` or `CardType.ALL_DIGITS` if " +
                    "unknown");
        }
        this.mCardNumberFormat = cardNumberFormat;
        this.mCardNumberView.setText(getFormattedCardNumber(mCardNumber));
        redrawViews();
    }

    @ColorInt
    public int getCardNameTextColor() {
        return mCardNameTextColor;
    }

    public void setCardNameTextColor(@ColorInt int cardNameTextColor) {
        this.mCardNameTextColor = cardNameTextColor;
        this.mCardNameView.setTextColor(mCardNameTextColor);
        redrawViews();
    }

    public String getExpiryDate() {
        return mExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.mExpiryDate = expiryDate;
        this.mExpiryDateView.setText(mExpiryDate);
        redrawViews();
    }

    @ColorInt
    public int getExpiryDateTextColor() {
        return mExpiryDateTextColor;
    }

    public void setExpiryDateTextColor(@ColorInt int expiryDateTextColor) {
        this.mExpiryDateTextColor = expiryDateTextColor;
        this.mExpiryDateView.setTextColor(mExpiryDateTextColor);
        redrawViews();
    }

    @ColorInt
    public int getValidTillTextColor() {
        return mValidTillTextColor;
    }

    public void setValidTillTextColor(@ColorInt int validTillTextColor) {
        this.mValidTillTextColor = validTillTextColor;
        this.mValidTill.setTextColor(mValidTillTextColor);
        redrawViews();
    }

    @CreditCardType
    public int getType() {
        return mType;
    }

    public void setType(@CreditCardType int type) {
        if (type < 0 | type > 4) {
            throw new UnsupportedOperationException("CardType: " + type + "  is not supported. " +
                    "Use `CardType.*` or `CardType.AUTO` if unknown");
        }
        this.mType = type;
        this.mCardTypeView.setBackgroundResource(getLogo());
        redrawViews();
    }

    public boolean getIsEditable() {
        return mIsEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.mIsEditable = isEditable;
        redrawViews();
    }

    public boolean getIsCardNameEditable() {
        return mIsCardNameEditable;
    }

    public void setIsCardNameEditable(boolean isCardNameEditable) {
        this.mIsCardNameEditable = isCardNameEditable;
        redrawViews();
    }

    public boolean getIsCardNumberEditable() {
        return mIsCardNumberEditable;
    }

    public void setIsCardNumberEditable(boolean isCardNumberEditable) {
        this.mIsCardNumberEditable = isCardNumberEditable;
        redrawViews();
    }

    public boolean getIsExpiryDateEditable() {
        return mIsExpiryDateEditable;
    }

    public void setIsExpiryDateEditable(boolean isExpiryDateEditable) {
        this.mIsExpiryDateEditable = isExpiryDateEditable;
        redrawViews();
    }

    @ColorInt
    public int getHintTextColor() {
        return mHintTextColor;
    }

    public void setHintTextColor(@ColorInt int hintTextColor) {
        this.mHintTextColor = hintTextColor;
        this.mCardNameView.setHintTextColor(mHintTextColor);
        this.mCardNumberView.setHintTextColor(mHintTextColor);
        this.mExpiryDateView.setHintTextColor(mHintTextColor);

        redrawViews();
    }

    @DrawableRes
    public int getBrandLogo() {
        return mBrandLogo;
    }

    public void setBrandLogo(@DrawableRes int brandLogo) {
        this.mBrandLogo = brandLogo;
        this.mBrandLogoView.setBackgroundResource(mBrandLogo);
        redrawViews();
    }

    public int getBrandLogoPosition() {
        return mBrandLogo;
    }

    public void setBrandLogoPosition(int brandLogoPosition) {
        redrawViews();
    }

    public void putChip(boolean flag) {
        this.mPutChip = flag;
        this.mChipView.setVisibility(mPutChip ? View.VISIBLE : View.GONE);
        redrawViews();
    }

    public boolean getIsCvvEditable() {
        return mIsCvvEditable;
    }

    public void setIsCvvEditable(boolean editable) {
        this.mIsCvvEditable = editable;
        redrawViews();
    }

    @DrawableRes
    public int getCardBackBackground() {
        return mCardBackBackground;
    }

    public void setCardBackBackground(@DrawableRes int cardBackBackground) {
        this.mCardBackBackground = cardBackBackground;
        setBackgroundResource(mCardBackBackground);
        redrawViews();
    }

    public int getCardFrontBackground() {
        return mCardFrontBackground;
    }

    public void setCardFrontBackground(@DrawableRes int mCardFrontBackground) {
        this.mCardFrontBackground = mCardFrontBackground;
        setBackgroundResource(mCardFrontBackground);
        redrawViews();
    }

    public String getFontPath() {
        return mFontPath;
    }

    public void setFontPath(String mFontPath) {
        this.mFontPath = mFontPath;
        if (!isInEditMode()) {
            // Loading Font Face
            mCreditCardTypeFace = Typeface.createFromAsset(mContext.getAssets(), mFontPath);
            mCardNumberView.setTypeface(mCreditCardTypeFace);
            mCardNameView.setTypeface(mCreditCardTypeFace);
            mExpiryDateView.setTypeface(mCreditCardTypeFace);
            mCvvView.setTypeface(mCreditCardTypeFace);
        }
        redrawViews();
    }

    /**
     * Return the appropriate drawable resource based on the card type
     */
    @DrawableRes
    private int getLogo() {

        switch (mType) {
            case VISA:
                return R.drawable.visa;

            case MASTERCARD:
                return R.drawable.mastercard;

            case AMERICAN_EXPRESS:
                return R.drawable.amex;

            case DISCOVER:
                return R.drawable.discover;

            case AUTO:
                return findCardType();

            default:
                throw new UnsupportedOperationException("CardType: " + mType + "  is not supported" +
                        ". Use `CardType.*` or `CardType.AUTO` if unknown");
        }

    }

    /**
     * Returns the formatted card number based on the user entered value for card number format
     *
     * @param cardNumber Card Number.
     */
    private String getFormattedCardNumber(String cardNumber) {

        if (DEBUG) {
            Log.e("Card Number", cardNumber);
        }

        switch (getCardNumberFormat()) {
            case MASKED_ALL_BUT_LAST_FOUR:
                cardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
                break;
            case ONLY_LAST_FOUR:
                cardNumber = cardNumber.substring(cardNumber.length() - 4);
                break;
            case MASKED_ALL:
                cardNumber = "**** **** **** ****";
                break;
            case ALL_DIGITS:
                //do nothing.
                break;
            default:
                throw new UnsupportedOperationException("CreditCardFormat: " + mCardNumberFormat +
                        " is not supported. Use `CreditCardFormat.*`");
        }
        return cardNumber;
    }

    /**
     * Returns the appropriate card type drawable resource based on the regex pattern of the card
     * number
     */
    @DrawableRes
    private int findCardType() {
        this.mType = VISA;
        if (!TextUtils.isEmpty(mCardNumber)) {
            final String cardNumber = mCardNumber.replaceAll("\\s+", "");

            if (Pattern.compile(PATTERN_MASTER_CARD).matcher(cardNumber).matches()) {
                this.mType = MASTERCARD;
            } else if (Pattern.compile(PATTERN_AMERICAN_EXPRESS).matcher(cardNumber).matches()) {
                this.mType = AMERICAN_EXPRESS;
            } else if (Pattern.compile(PATTERN_DISCOVER).matcher(cardNumber).matches()) {
                this.mType = DISCOVER;
            }
        }
        return getLogo();
    }

    /**
     * Adds space after every 4 characters to the card number if the card number is divisible by 4
     */
    private String addSpaceToCardNumber() {

        final int splitBy = 4;
        final int length = mCardNumber.length();

        if (length % splitBy != 0 || length <= splitBy) {
            return mCardNumber;
        } else {
            final StringBuilder result = new StringBuilder();
            result.append(mCardNumber.substring(0, splitBy));
            for (int i = splitBy; i < length; i++) {
                if (i % splitBy == 0) {
                    result.append(" ");
                }
                result.append(mCardNumber.charAt(i));
            }
            return result.toString();
        }
    }

    @TargetApi(11)
    private void rotateInToBack() {
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator rotateIn = ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final ObjectAnimator hideFrontView = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideFrontView.setDuration(1);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateOutToBack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.play(hideFrontView).after(rotateIn);
        set.start();
    }

    @TargetApi(11)
    private void rotateInToFront() {
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator rotateIn = ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final ObjectAnimator hideBackView = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setDuration(300);
        hideBackView.setDuration(1);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotateOutToFront();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.play(hideBackView).after(rotateIn);
        set.start();
    }

    @TargetApi(11)
    private void rotateOutToBack() {
        hideFrontView();
        showBackView();
        CreditCardView.this.setRotationY(-90);
        setBackgroundResource(mCardBackBackground);
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator flipView = ObjectAnimator.ofInt(CreditCardView.this, "rotationY", 90, -90);
        final ObjectAnimator rotateOut = ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        final ObjectAnimator showBackView = ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        flipView.setDuration(0);
        showBackView.setDuration(1);
        rotateOut.setDuration(300);
        showBackView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardSide = CARD_BACK;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Do nothing
            }
        });
        set.play(flipView).with(showBackView).before(rotateOut);
        set.start();
    }

    @TargetApi(11)
    private void rotateOutToFront() {
        showFrontView();
        hideBackView();
        CreditCardView.this.setRotationY(-90);
        setBackgroundResource(mCardFrontBackground);
        AnimatorSet set = new AnimatorSet();
        final ObjectAnimator flipView = ObjectAnimator.ofInt(CreditCardView.this, "rotationY", 90, -90);
        final ObjectAnimator rotateOut = ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        final ObjectAnimator showFrontView = ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        showFrontView.setDuration(1);
        rotateOut.setDuration(300);
        showFrontView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //Do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cardSide = CARD_FRONT;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //Do nothing
            }
        });
        set.play(flipView).with(showFrontView).before(rotateOut);
        set.start();
    }

    private void rotateInToBackBeforeEleven() {
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        final com.nineoldandroids.animation.ObjectAnimator rotateIn = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final com.nineoldandroids.animation.ObjectAnimator hideFrontView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideFrontView.setDuration(1);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                rotateOutToBackBeforeEleven();
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(hideFrontView).after(rotateIn);
        set.start();
    }

    private void rotateInToFrontBeforeEleven() {
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        final com.nineoldandroids.animation.ObjectAnimator rotateIn = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "rotationY", 0, 90);
        final com.nineoldandroids.animation.ObjectAnimator hideBackView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        rotateIn.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateIn.setDuration(300);
        hideBackView.setDuration(1);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                rotateOutToFrontBeforeEleven();
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(hideBackView).after(rotateIn);
        set.start();
    }

    private void rotateOutToBackBeforeEleven() {
        hideFrontView();
        showBackView();
        setBackgroundResource(mCardBackBackground);
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        com.nineoldandroids.animation.ObjectAnimator flip = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", 90, -90);
        com.nineoldandroids.animation.ObjectAnimator rotateOut = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        com.nineoldandroids.animation.ObjectAnimator showBackView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        flip.setDuration(0);
        showBackView.setDuration(1);
        rotateOut.setDuration(300);
        showBackView.setStartDelay(150);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                cardSide = CARD_BACK;
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(flip).with(showBackView).before(rotateOut);
        set.start();
    }

    private void rotateOutToFrontBeforeEleven() {
        showFrontView();
        hideBackView();
        setBackgroundResource(R.drawable.cardbackground_sky);
        com.nineoldandroids.animation.AnimatorSet set = new com.nineoldandroids.animation.AnimatorSet();
        com.nineoldandroids.animation.ObjectAnimator flip = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", 90, -90);
        com.nineoldandroids.animation.ObjectAnimator rotateOut = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "rotationY", -90, 0);
        com.nineoldandroids.animation.ObjectAnimator showFrontView = com.nineoldandroids.animation.ObjectAnimator.ofFloat(CreditCardView.this, "alpha", 0, 1);
        showFrontView.setDuration(1);
        rotateOut.setDuration(300);
        rotateOut.setInterpolator(new AccelerateDecelerateInterpolator());
        showFrontView.setStartDelay(150);
        set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                cardSide = CARD_FRONT;
            }

            @Override
            public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

            }
        });
        set.play(flip).with(showFrontView).with(rotateOut);
        set.start();
    }

    @IntDef({VISA, MASTERCARD, AMERICAN_EXPRESS, DISCOVER, AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CreditCardType {
    }

    @IntDef({ALL_DIGITS, MASKED_ALL_BUT_LAST_FOUR, ONLY_LAST_FOUR, MASKED_ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CreditCardFormat {
    }
}
