package com.example.vinay.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
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

/**
 * Created by vgaba on 4/28/2015.
 */
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

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.creditcardview, this, true);



        // Font path
        String fontPath = "fonts/creditcard2.ttf";
        // Loading Font Face
        creditCardTypeFace = Typeface.createFromAsset(getContext().getAssets(), fontPath);

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
                R.styleable.CreditCardView2,
                0, 0);

        try {
            mCardNumber = a.getString(R.styleable.CreditCardView2_cardNumber);
            mCardName = a.getString(R.styleable.CreditCardView2_cardName);
            mExpiryDate = a.getString(R.styleable.CreditCardView2_expiryDate);
            mCardNumberTextColor = a.getColor(R.styleable.CreditCardView2_cardNumberTextColor, Color.WHITE);
            mCardNumberFormat = a.getInt(R.styleable.CreditCardView2_cardNumberFormat, 0);
            mCardNameTextColor = a.getColor(R.styleable.CreditCardView2_cardNumberTextColor, Color.WHITE);
            mExpiryDateTextColor = a.getColor(R.styleable.CreditCardView2_expiryDateTextColor, Color.WHITE);
            mValidTillTextColor = a.getColor(R.styleable.CreditCardView2_validTillTextColor, Color.WHITE);
            mType = a.getInt(R.styleable.CreditCardView2_type, 0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView2_brandLogo, 0);
            mPutChip = a.getBoolean(R.styleable.CreditCardView2_putChip, false);
            mIsEditable = a.getBoolean(R.styleable.CreditCardView2_isEditable,false);
            mHintTextColor = a.getColor(R.styleable.CreditCardView2_hintTextColor, Color.WHITE);
        } finally {
            a.recycle();
        }

        //Set default background
        if(getBackground()==null){
            setBackgroundResource(R.drawable.cardbackground_sky);
        }

        if(!mIsEditable){

            cardNumber.setEnabled(false);
            cardName.setEnabled(false);
            expiryDate.setEnabled(false);
        }
        else{
            cardNumber.setHint(R.string.card_number_hint);
            cardNumber.setHintTextColor(mHintTextColor);

            cardName.setHint(R.string.card_name_hint);
            cardName.setHintTextColor(mHintTextColor);

            expiryDate.setHint(R.string.expiry_date_hint);
            expiryDate.setHintTextColor(mHintTextColor);
        }

        cardNumber.setText(checkCardNumberFormat(addSpaceToCardNumber(mCardNumber)));
        cardNumber.setTextColor(mCardNumberTextColor);
        cardNumber.setTypeface(creditCardTypeFace);


        cardName.setText(mCardName.toUpperCase());
        cardName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cardName.setTextColor(mCardNumberTextColor);
        cardName.setTypeface(creditCardTypeFace);


        //Set the appropriate logo based on the type of card
        type.setBackgroundResource(getLogo(mType));

        //If background logo attribute is present, set it
        if(mBrandLogo != 0)
            brandLogo.setBackgroundResource(mBrandLogo);

        //If putChip attribute is present, change the visibility of the view and display it
        if(mPutChip){
            chip = (ImageView)getChildAt(4);
            chip.setVisibility(View.VISIBLE);
        }

        expiryDate.setText(mExpiryDate);
        expiryDate.setTextColor(mExpiryDateTextColor);
        expiryDate.setTypeface(creditCardTypeFace);


        validTill.setTextColor(mValidTillTextColor);



        cardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mType =4;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

                mCardNumber = cardNumber.getText().toString();
                if(mType ==4){
                    type.setBackgroundResource(getLogo(mType));
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
                mExpiryDate = expiryDate.getText().toString();

            }

        });

    }



    public String getCardNumber(){

        return mCardNumber;
    }

    public void setCardNumber(String cardNumber){
        mCardNumber = cardNumber;
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

    public void putChip(){
        mPutChip = true;
        invalidate();
        requestLayout();
    }

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

    public int findCardType(){
        String cardNumber = getCardNumber();
        cardNumber = cardNumber.replaceAll("\\s+","");
        int type = 0;

        if(Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
            type = 0;
        else if(Pattern.compile("^5[1-5][0-9]{14}$").matcher(cardNumber).matches())
            type = 1;
        else if(Pattern.compile("^3[47][0-9]{13}$").matcher(cardNumber).matches())
            type = 2;
        else if(Pattern.compile("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$").matcher(cardNumber).matches())
            type = 3;
        else
            type = 0;

        setType(type);

        return getLogo(type);
    }

    public String addSpaceToCardNumber(String cardNumber){

        if(cardNumber.length()<6){

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
