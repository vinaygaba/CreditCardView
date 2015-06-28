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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
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

import java.util.regex.Pattern;


public class CreditCardView extends RelativeLayout{

    private String mCardNumber = "";
    private String mCardName = "";
    private String mExpiryDate = "";
    private int mCardNumberTextColor = Color.WHITE;
    private int mCardNumberFormat = 0;
    private int mCardNameTextColor = Color.WHITE;
    private int mExpiryDateTextColor = Color.WHITE;
    private int mValidTillTextColor = Color.WHITE;
    private int mType = 0;
    private int mBrandLogo;
    private int mBrandLogoPosition = 1;
    private boolean mPutChip = false;
    private boolean mIsEditable=false;
    private int mHintTextColor = Color.WHITE;
    private Typeface creditCardTypeFace;
    private EditText cardNumber;
    private EditText cardName;
    private EditText expiryDate;
    private TextView validTill;
    private ImageView type;
    ImageView brandLogo;
    ImageView chip;



    public CreditCardView(Context context) {
        super(context);
        init();

    }

    public CreditCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(attrs);
    }

    /**
     * Initialize various views and variables
     */
    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.creditcardview, this, true);

        //Added this check to fix the issue of custom view not rendering correctly in the layout preview.
        if(!isInEditMode()) {
            // Font path
            String fontPath = "fonts/halter.ttf";
            // Loading Font Face
            creditCardTypeFace = Typeface.createFromAsset(getContext().getAssets(), fontPath);

        }
        cardNumber = (EditText)getChildAt(0);

        cardName = (EditText)getChildAt(1);

        type = (ImageView)getChildAt(2);

        brandLogo = (ImageView)getChildAt(3);

        chip = (ImageView)getChildAt(4);

        validTill = (TextView)getChildAt(5);

        expiryDate = (EditText)getChildAt(6);
    }

    private void loadAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CreditCardView,
                0, 0);

        try {
            mCardNumber = a.getString(R.styleable.CreditCardView_cardNumber);
            mCardName = a.getString(R.styleable.CreditCardView_cardName);
            mExpiryDate = a.getString(R.styleable.CreditCardView_expiryDate);
            mCardNumberTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor, Color.WHITE);
            mCardNumberFormat = a.getInt(R.styleable.CreditCardView_cardNumberFormat, 0);
            mCardNameTextColor = a.getColor(R.styleable.CreditCardView_cardNumberTextColor, Color.WHITE);
            mExpiryDateTextColor = a.getColor(R.styleable.CreditCardView_expiryDateTextColor, Color.WHITE);
            mValidTillTextColor = a.getColor(R.styleable.CreditCardView_validTillTextColor, Color.WHITE);
            mType = a.getInt(R.styleable.CreditCardView_type, 0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView_brandLogo, 0);
          //mBrandLogoPosition = a.getInt(R.styleable.CreditCardView_brandLogoPosition, 1);
            mPutChip = a.getBoolean(R.styleable.CreditCardView_putChip, false);
            mIsEditable = a.getBoolean(R.styleable.CreditCardView_isEditable,false);
            mHintTextColor = a.getColor(R.styleable.CreditCardView_hintTextColor, Color.WHITE);
        } finally {
            a.recycle();
        }

        //Set default background if background attribute was not entered in the xml
        if(getBackground()==null){
            setBackgroundResource(R.drawable.cardbackground_sky);
        }


        if(!mIsEditable){
            //If card is not set to be editable, disable the edit texts
            cardNumber.setEnabled(false);
            cardName.setEnabled(false);
            expiryDate.setEnabled(false);
        }
        else{
            //If the card is editable, set the hin text and hint values which will be displayed when the edit text is blank
            cardNumber.setHint(R.string.card_number_hint);
            cardNumber.setHintTextColor(mHintTextColor);

            cardName.setHint(R.string.card_name_hint);
            cardName.setHintTextColor(mHintTextColor);

            expiryDate.setHint(R.string.expiry_date_hint);
            expiryDate.setHintTextColor(mHintTextColor);
        }

        //If card number is not null, add space every 4 characters and format it in the appropriate format
        if(mCardNumber!= null)
            cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));

        //Set the user entered card number color to card number field
        cardNumber.setTextColor(mCardNumberTextColor);

        //Added this check to fix the issue of custom view not rendering correctly in the layout preview.
        if(!isInEditMode()) {
            cardNumber.setTypeface(creditCardTypeFace);
        }

        //If card name is not null, convert the text to upper case
        if(mCardName!= null)
            cardName.setText(mCardName.toUpperCase());

        //This filter will ensure the text entered is in uppercase when the user manually enters the card name
        cardName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        //Set the user entered card name color to card name field
        cardName.setTextColor(mCardNumberTextColor);

        //Added this check to fix the issue of custom view not rendering correctly in the layout preview.
        if(!isInEditMode()) {
            cardName.setTypeface(creditCardTypeFace);
        }

        //Set the appropriate logo based on the type of card
        type.setBackgroundResource(getLogo(mType));

        //If background logo attribute is present, set it as the brand logo background resource
        if(mBrandLogo != 0) {
            brandLogo.setBackgroundResource(mBrandLogo);
           // brandLogo.setLayoutParams(params);
        }

        //If putChip attribute is present, change the visibility of the putChip view and display it
        if(mPutChip){
            chip = (ImageView)getChildAt(4);
            chip.setVisibility(View.VISIBLE);
        }

        //If expiry date is not null, set it to the expiryDate TextView
        if(mExpiryDate!= null)
            expiryDate.setText(mExpiryDate);

        //Set the user entered expiry date color to expiry date field
        expiryDate.setTextColor(mExpiryDateTextColor);

        //Added this check to fix the issue of custom view not rendering correctly in the layout preview.
        if(!isInEditMode()) {
            expiryDate.setTypeface(creditCardTypeFace);
        }

        //Set the appropriate text color to the validTill TextView
        validTill.setTextColor(mValidTillTextColor);

        //Add text change listener
        cardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //Change card type to auto to dynamically detect the card type based on the card number
                mType =4;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Delete any spaces the user might have entered manually. The library automatically adds spaces after every 4 characters to the view.
                mCardNumber = cardNumber.getText().toString().replaceAll("\\s+","");

            }


        });

        //Add focus change listener to detect focus being shifted from the cardNumber EditText
        cardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                //If the field just lost focus
                if (!hasFocus) {
                    if (mCardNumber.length() > 12) {
                        //If the length of card is >12, add space every 4 characters and format it in the appropriate format
                        cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));

                        //If card type is "auto",find the appropriate logo
                        if (mType == 4) {
                            type.setBackgroundResource(getLogo(mType));
                        }
                    }
                }
            }
        });


        cardName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Set the mCardName attribute the user entered value in the Card Name field
                mCardName = cardName.getText().toString().toUpperCase();

            }

        });

        expiryDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //Set the mExpiryDate attribute the user entered value in the Expiry Date field
                mExpiryDate = expiryDate.getText().toString();

            }

        });

    }



    public String getCardNumber(){

        return mCardNumber;
    }

    public void setCardNumber(String cardNumber){
        mCardNumber = cardNumber.replaceAll("\\s+","");
        invalidate();
        requestLayout();
    }

    public String getCardName(){

        return mCardName;
    }

    public void setCardName(String cardName){
        mCardName = cardName.toUpperCase();
        invalidate();
        requestLayout();
    }

    public int getCardNumberTextColor(){

        return mCardNumberTextColor;
    }

    public void setCardNumberTextColor(int cardNumberTextColor){
        mCardNumberTextColor = cardNumberTextColor;
        invalidate();
        requestLayout();
    }

    public int getCardNumberFormat(){

        return mCardNumberFormat;
    }

    public void setCardNumberFormat(int cardNumberFormat){
        mCardNumberFormat = cardNumberFormat;
        invalidate();
        requestLayout();
    }

    public int getCardNameTextColor(){

        return mCardNameTextColor;
    }

    public void setCardNameTextColor(int cardNameTextColor){
        mCardNameTextColor = cardNameTextColor;
        invalidate();
        requestLayout();
    }

    public String getExpiryDate(){

        return mExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        mExpiryDate = expiryDate;
        invalidate();
        requestLayout();
    }

    public int getExpiryDateTextColor(){

        return mExpiryDateTextColor;
    }

    public void setExpiryDateTextColor(int expiryDateTextColor){
        mExpiryDateTextColor = expiryDateTextColor;
        invalidate();
        requestLayout();
    }

    public int getValidTillTextColor(){

        return mValidTillTextColor;
    }

    public void setValidTillTextColor(int validTillTextColor){
        mValidTillTextColor = validTillTextColor;
        invalidate();
        requestLayout();
    }



    public int getType(){

        return mType;
    }

    public void setType(int type){

        mType = type;
        invalidate();
        requestLayout();
    }

    public boolean getIsEditable(){

        return mIsEditable;
    }

    public void setIsEditable(boolean isEditable){

        mIsEditable = isEditable;
        invalidate();
        requestLayout();
    }

    public int getHintTextColor(){

        return mHintTextColor;
    }

    public void setHintTextColor(int hintTextColor){
        mHintTextColor = hintTextColor;
        invalidate();
        requestLayout();
    }

    public int getBrandLogo(){

        return mBrandLogo;
    }

    public void setBrandLogo(int brandLogo){

        mBrandLogo = brandLogo;
        invalidate();
        requestLayout();
    }

    public int getBrandLogoPosition(){

        return mBrandLogo;
    }

    public void setBrandLogoposition(int brandLogoPosition){

        mBrandLogoPosition = brandLogoPosition;
        invalidate();
        requestLayout();
    }

    public void putChip(boolean flag){
        mPutChip = flag;
        invalidate();
        requestLayout();
    }

    /**
     *Return the appropriate drawable resource based on the card type
     *
     * @param type
     */
    public int getLogo(int type){

        switch(type){
            case 0: return R.drawable.visa;


            case 1: return R.drawable.mastercard;


            case 2: return R.drawable.amex;

            case 3: return R.drawable.discover;

            case 4: return findCardType();

        }

        return 0;
    }

    /**
     * Returns the formatted card number based on the user entered value for card number format
     *
     * @param cardNumber
     */
    public String checkCardNumberFormat(String cardNumber){
        Log.e("Card Number",cardNumber);
        if(getCardNumberFormat()==1){

            cardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4,cardNumber.length());
        }
        else if(getCardNumberFormat()==2){

            cardNumber = cardNumber.substring(cardNumber.length() - 4,cardNumber.length());
        }

        else if(getCardNumberFormat()==3){

            cardNumber = "**** **** **** ****";
        }

        return cardNumber;
    }

    /**
     * Returns the appropriate card type drawable resource based on the regex pattern of the card number
     */
    public int findCardType(){

        int type = 0;
        if(cardNumber.length()>0) {
            String cardNumber = getCardNumber();
            cardNumber = cardNumber.replaceAll("\\s+", "");

            if (Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
                type = 0;
            else if (Pattern.compile("^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
                type = 1;
            else if (Pattern.compile("^3[47][0-9]{13}$").matcher(cardNumber).matches())
                type = 2;
            else if (Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$").matcher(cardNumber).matches())
                type = 3;
            else
                type = 0;
        }
        setType(type);

        return getLogo(type);
    }

    /**
     * Adds space after every 4 characters to the card number if the card number is divisible by 4
     *
     * @param cardNumber
     */
    public String addSpaceToCardNumber(String cardNumber){

        if(cardNumber.length()%4 !=0){

            return cardNumber;
        }
        else {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < cardNumber.length(); i++) {
                if (i % 4 == 0 && i != 0 && i!= cardNumber.length()-1) {
                    result.append(" ");
                }

                result.append(cardNumber.charAt(i));
            }

            return result.toString();
        }
    }
}
