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

import android.annotation.SuppressLint;
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
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @IntDef({VISA, MASTERCARD, AMERICAN_EXPRESS, DISCOVER, AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CreditCardType {
    }

    @IntDef({ALL_DIGITS, MASKED_ALL_BUT_LAST_FOUR, ONLY_LAST_FOUR, MASKED_ALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CreditCardFormat {
    }

    private static final boolean DEBUG = false;
    private Context mContext;
    private String mCardNumber = "";
    private String mCardName = "";
    private String mExpiryDate = "";
    private int mCardNumberTextColor = Color.WHITE;
    private int mCardNumberFormat = ALL_DIGITS;
    private int mCardNameTextColor = Color.WHITE;
    private int mExpiryDateTextColor = Color.WHITE;
    private int mValidTillTextColor = Color.WHITE;
    private int mType = VISA;
    private int mBrandLogo;
    private boolean mPutChip = false;
    private boolean mIsEditable = false;
    private boolean mIsCardNumberEditable = false;
    private boolean mIsCardNameEditable = false;
    private boolean mIsExpiryDateEditable = false;
    private int mHintTextColor = Color.WHITE;
    private Typeface creditCardTypeFace;
    private EditText cardNumber;
    private EditText cardName;
    private EditText expiryDate;
    private TextView validTill;
    private ImageView type;
    private ImageView brandLogo;
    private ImageView chip;

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
        final LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.creditcardview, this, true);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            // Font path
            final String fontPath = mContext.getString(R.string.font_path);
            // Loading Font Face
            creditCardTypeFace = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        }

        cardNumber = (EditText) findViewById(R.id.card_number);
        cardName = (EditText) findViewById(R.id.card_name);
        type = (ImageView) findViewById(R.id.card_logo);
        brandLogo = (ImageView) findViewById(R.id.brand_logo);
        chip = (ImageView) findViewById(R.id.chip);
        validTill = (TextView) findViewById(R.id.valid_till);
        expiryDate = (EditText) findViewById(R.id.expiry_date);
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
            mValidTillTextColor = a.getColor(R.styleable.CreditCardView_validTillTextColor,
                    Color.WHITE);
            mType = a.getInt(R.styleable.CreditCardView_type, 0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView_brandLogo, 0);
            // mBrandLogoPosition = a.getInt(R.styleable.CreditCardView_brandLogoPosition, 1);
            mPutChip = a.getBoolean(R.styleable.CreditCardView_putChip, false);
            mIsEditable = a.getBoolean(R.styleable.CreditCardView_isEditable, false);
            //For more granular control to the fields. Issue #7
            mIsCardNameEditable = a.getBoolean(R.styleable.CreditCardView_isCardNameEditable, mIsEditable);
            mIsCardNumberEditable = a.getBoolean(R.styleable.CreditCardView_isCardNumberEditable, mIsEditable);
            mIsExpiryDateEditable = a.getBoolean(R.styleable.CreditCardView_isExpiryDateEditable, mIsEditable);
            mHintTextColor = a.getColor(R.styleable.CreditCardView_hintTextColor, Color.WHITE);
        } finally {
            a.recycle();
        }
    }

    private void initDefaults() {

        // Set default background if background attribute was not entered in the xml
        if (getBackground() == null) {
            setBackgroundResource(R.drawable.cardbackground_sky);
        }

        if (!mIsEditable) {
            // If card is not set to be editable, disable the edit texts
            cardNumber.setEnabled(false);
            cardName.setEnabled(false);
            expiryDate.setEnabled(false);
        } else {
            // If the card is editable, set the hint text and hint values which will be displayed
            // when the edit text is blank
            cardNumber.setHint(R.string.card_number_hint);
            cardNumber.setHintTextColor(mHintTextColor);

            cardName.setHint(R.string.card_name_hint);
            cardName.setHintTextColor(mHintTextColor);

            expiryDate.setHint(R.string.expiry_date_hint);
            expiryDate.setHintTextColor(mHintTextColor);
        }

        //For more granular control of the editable fields. Issue #7
        if(mIsCardNameEditable!=mIsEditable){
            //If the mIsCardNameEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if(mIsCardNameEditable){
                cardName.setHint(R.string.card_name_hint);
                cardName.setHintTextColor(mHintTextColor);
            }
            else{
                cardName.setHint("");
            }

            cardName.setEnabled(mIsCardNameEditable);
        }

        if(mIsCardNumberEditable!=mIsEditable){
            //If the mIsCardNumberEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if(mIsCardNumberEditable){
                cardNumber.setHint(R.string.card_number_hint);
                cardNumber.setHintTextColor(mHintTextColor);
            }
            else{
                cardNumber.setHint("");
            }
            cardNumber.setEnabled(mIsCardNumberEditable);
        }

        if(mIsExpiryDateEditable!=mIsEditable){
            //If the mIsExpiryDateEditable is different than mIsEditable field, the granular
            //precedence comes into picture and the value needs to be checked and modified
            //accordingly
            if(mIsExpiryDateEditable){
                expiryDate.setHint(R.string.expiry_date_hint);
                expiryDate.setHintTextColor(mHintTextColor);
            }
            else{
                expiryDate.setHint("");
            }
            expiryDate.setEnabled(mIsExpiryDateEditable);
        }

        // If card number is not null, add space every 4 characters and format it in the appropriate
        // format
        if (mCardNumber != null) {
            cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));
        }

        // Set the user entered card number color to card number field
        cardNumber.setTextColor(mCardNumberTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            cardNumber.setTypeface(creditCardTypeFace);
        }

        // If card name is not null, convert the text to upper case
        if (mCardName != null) {
            cardName.setText(mCardName.toUpperCase());
        }

        // This filter will ensure the text entered is in uppercase when the user manually enters
        // the card name
        cardName.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });

        // Set the user entered card name color to card name field
        cardName.setTextColor(mCardNumberTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            cardName.setTypeface(creditCardTypeFace);
        }

        // Set the appropriate logo based on the type of card
        type.setBackgroundResource(getLogo(mType));

        // If background logo attribute is present, set it as the brand logo background resource
        if (mBrandLogo != 0) {
            brandLogo.setBackgroundResource(mBrandLogo);
            // brandLogo.setLayoutParams(params);
        }

        // If putChip attribute is present, change the visibility of the putChip view and display it
        if (mPutChip) {
            chip.setVisibility(View.VISIBLE);
        }

        // If expiry date is not null, set it to the expiryDate TextView
        if (mExpiryDate != null) {
            expiryDate.setText(mExpiryDate);
        }

        // Set the user entered expiry date color to expiry date field
        expiryDate.setTextColor(mExpiryDateTextColor);

        // Added this check to fix the issue of custom view not rendering correctly in the layout
        // preview.
        if (!isInEditMode()) {
            expiryDate.setTypeface(creditCardTypeFace);
        }

        // Set the appropriate text color to the validTill TextView
        validTill.setTextColor(mValidTillTextColor);
    }

    private void addListeners() {

        // Add text change listener
        cardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Change card type to auto to dynamically detect the card type based on the card
                // number
                mType = AUTO;
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
        cardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If the field just lost focus
                if (!hasFocus) {
                    //Fix for NPE. Issue #6
                    if(mCardNumber != null) {
                        if (mCardNumber.length() > 12) {
                            // If the length of card is >12, add space every 4 characters and format it
                            // in the appropriate format
                            cardNumber
                                    .setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));

                            // If card type is "auto",find the appropriate logo
                            if (mType == AUTO) {
                                type.setBackgroundResource(getLogo(mType));
                            }
                        }
                    }
                }
            }
        });

        cardName.addTextChangedListener(new TextWatcher() {

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

        expiryDate.addTextChangedListener(new TextWatcher() {

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
    }

    private void redrawViews() {
        invalidate();
        requestLayout();
    }

    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        mCardNumber = cardNumber.replaceAll("\\s+", "");
        redrawViews();
    }

    public String getCardName() {
        return mCardName;
    }

    public void setCardName(String cardName) {
        mCardName = cardName.toUpperCase();
        redrawViews();
    }

    @ColorInt
    public int getCardNumberTextColor() {
        return mCardNumberTextColor;
    }

    public void setCardNumberTextColor(@ColorInt int cardNumberTextColor) {
        mCardNumberTextColor = cardNumberTextColor;
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
        mCardNumberFormat = cardNumberFormat;
        redrawViews();
    }

    @ColorInt
    public int getCardNameTextColor() {
        return mCardNameTextColor;
    }

    public void setCardNameTextColor(@ColorInt int cardNameTextColor) {
        mCardNameTextColor = cardNameTextColor;
        redrawViews();
    }

    public String getExpiryDate() {
        return mExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        mExpiryDate = expiryDate;
        redrawViews();
    }

    @ColorInt
    public int getExpiryDateTextColor() {
        return mExpiryDateTextColor;
    }

    public void setExpiryDateTextColor(@ColorInt int expiryDateTextColor) {
        mExpiryDateTextColor = expiryDateTextColor;
        redrawViews();
    }

    @ColorInt
    public int getValidTillTextColor() {
        return mValidTillTextColor;
    }

    public void setValidTillTextColor(@ColorInt int validTillTextColor) {
        mValidTillTextColor = validTillTextColor;
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
        mType = type;
        redrawViews();
    }

    public boolean getIsEditable() {
        return mIsEditable;
    }

    public void setIsEditable(boolean isEditable) {
        mIsEditable = isEditable;
        redrawViews();
    }

    public boolean getIsCardNameEditable() {
        return mIsCardNameEditable;
    }

    public void setIsCardNameEditable(boolean isCardNameEditable) {
        mIsCardNameEditable = isCardNameEditable;
        redrawViews();
    }

    public boolean getIsCardNumberEditable() {
        return mIsCardNumberEditable;
    }

    public void setIsCardNumberEditable(boolean isCardNumberEditable) {
        mIsCardNumberEditable = isCardNumberEditable;
        redrawViews();
    }

    public boolean getIsExpiryDateEditable() {
        return mIsExpiryDateEditable;
    }

    public void setIsExpiryDateEditable(boolean isExpiryDateEditable) {
        mIsExpiryDateEditable = isExpiryDateEditable;
        redrawViews();
    }

    @ColorInt
    public int getHintTextColor() {
        return mHintTextColor;
    }

    public void setHintTextColor(@ColorInt int hintTextColor) {
        mHintTextColor = hintTextColor;
        redrawViews();
    }

    @DrawableRes
    public int getBrandLogo() {
        return mBrandLogo;
    }

    public void setBrandLogo(@DrawableRes int brandLogo) {
        mBrandLogo = brandLogo;
        redrawViews();
    }

    public int getBrandLogoPosition() {
        return mBrandLogo;
    }

    public void setBrandLogoPosition(int brandLogoPosition) {
        redrawViews();
    }

    public void putChip(boolean flag) {
        mPutChip = flag;
        redrawViews();
    }

    /**
     * Return the appropriate drawable resource based on the card type
     *
     * @param type type of card.
     */
    @DrawableRes
    private int getLogo(@CreditCardType int type) {

        switch (type) {
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
                throw new UnsupportedOperationException("CardType: " + type + "  is not supported" +
                        ". Use `CardType.*` or `CardType.AUTO` if unknown");
        }

    }

    /**
     * Returns the formatted card number based on the user entered value for card number format
     *
     * @param cardNumber Card Number.
     */
    private String checkCardNumberFormat(String cardNumber) {

        if (DEBUG) {
            Log.e("Card Number", cardNumber);
        }

        switch (getCardNumberFormat()) {
            case MASKED_ALL_BUT_LAST_FOUR:
                cardNumber = "**** **** **** "
                        + cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
                break;
            case ONLY_LAST_FOUR:
                cardNumber = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
                break;
            case MASKED_ALL:
                cardNumber = "**** **** **** ****";
                break;
            default:
                //do nothing.
                break;
        }
        return cardNumber;
    }

    /**
     * Returns the appropriate card type drawable resource based on the regex pattern of the card
     * number
     */
    @DrawableRes
    private int findCardType() {

        int type = VISA;
        if (cardNumber.length() > 0) {

            final String cardNumber = getCardNumber().replaceAll("\\s+", "");

            if (Pattern.compile(PATTERN_MASTER_CARD).matcher(cardNumber).matches()) {
                type = MASTERCARD;
            } else if (Pattern.compile(PATTERN_AMERICAN_EXPRESS).matcher(cardNumber)
                    .matches()) {
                type = AMERICAN_EXPRESS;
            } else if (Pattern.compile(PATTERN_DISCOVER).matcher(cardNumber).matches()) {
                type = DISCOVER;
            }
        }
        setType(type);

        return getLogo(type);
    }

    /**
     * Adds space after every 4 characters to the card number if the card number is divisible by 4
     *
     * @param cardNumber Card Number.
     */
    private String addSpaceToCardNumber(String cardNumber) {

        if (cardNumber.length() % 4 != 0) {
            return cardNumber;
        } else {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < cardNumber.length(); i++) {
                if (i % 4 == 0 && i != 0 && i != cardNumber.length() - 1) {
                    result.append(" ");
                }
                result.append(cardNumber.charAt(i));
            }
            return result.toString();
        }
    }
}
