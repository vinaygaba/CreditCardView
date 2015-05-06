package com.example.vinay.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vgaba on 4/28/2015.
 */
public class CreditCardView extends RelativeLayout{

    private String mCardNumber = "0000 0000 0000 0000";
    private String mCardName = "John Doe";
    private String mExpiryDate = "01/11";
    private int mCardNumberTextColor = Color.WHITE;
    private int mCardNumberFormat = 0;
    private int mCardNameTextColor = Color.WHITE;
    private int mExpiryDateTextColor = Color.WHITE;
    private int mType = 0;
    private int mBrandLogo;
    private boolean mPutChip = false;
    private Typeface creditCardTypeFace;
    private TextView cardNumber;
    private TextView cardName;
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
        /*
        TypedArray a = context.getTheme().obtainStyledAttributes(
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
            mType = a.getInt(R.styleable.CreditCardView2_type,0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView2_brandLogo,0);
            mPutChip = a.getBoolean(R.styleable.CreditCardView2_putChip,false);
        } finally {
            a.recycle();
        }

        /*PaintDrawable pd = new PaintDrawable();
        pd.setCornerRadius(20);
        setBackgroundDrawable(pd);*/

        /*

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.creditcardview, this, true);

        //Set default background
        if(getBackground()==null){
            setBackgroundResource(R.drawable.cardbackground_sky);
        }
        // Font path
        String fontPath = "fonts/creditcard2.ttf";
        // Loading Font Face
        creditCardTypeFace = Typeface.createFromAsset(context.getAssets(), fontPath);

        cardNumber = (TextView)getChildAt(0);
        //Check card number format and change the value accordingly
        checkCardNumberFormat(mCardNumber);
        cardNumber.setText(mCardNumber);
        cardNumber.setTextColor(mCardNumberTextColor);
        cardNumber.setTypeface(creditCardTypeFace);

        cardName = (TextView)getChildAt(1);
        cardName.setText(mCardName);
        cardName.setTextColor(mCardNumberTextColor);



        type = (ImageView)getChildAt(2);
        //Set the appropriate logo based on the type of card
        type.setBackgroundResource(getLogo(mType));


        brandLogo = (ImageView)getChildAt(3);
        //If background logo attribute is present, set it
        if(mBrandLogo != 0)
            brandLogo.setBackgroundResource(mBrandLogo);

        //If putChip attribute is present, change the visibility of the view and display it
        if(mPutChip){
            chip = (ImageView)getChildAt(4);
            chip.setVisibility(View.VISIBLE);
        }
*/

    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.creditcardview, this, true);



        // Font path
        String fontPath = "fonts/creditcard2.ttf";
        // Loading Font Face
        creditCardTypeFace = Typeface.createFromAsset(getContext().getAssets(), fontPath);

        cardNumber = (TextView)getChildAt(0);

        cardName = (TextView)getChildAt(1);

        type = (ImageView)getChildAt(2);

        brandLogo = (ImageView)getChildAt(3);

        chip = (ImageView)getChildAt(4);
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
            mType = a.getInt(R.styleable.CreditCardView2_type,0);
            mBrandLogo = a.getResourceId(R.styleable.CreditCardView2_brandLogo,0);
            mPutChip = a.getBoolean(R.styleable.CreditCardView2_putChip,false);
        } finally {
            a.recycle();
        }

        //Set default background
        if(getBackground()==null){
            setBackgroundResource(R.drawable.cardbackground_sky);
        }

        checkCardNumberFormat(mCardNumber);
        cardNumber.setText(mCardNumber);
        cardNumber.setTextColor(mCardNumberTextColor);
        cardNumber.setTypeface(creditCardTypeFace);

        cardName.setText(mCardName);
        cardName.setTextColor(mCardNumberTextColor);

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
        mCardName = cardName;
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



    public int getType(){

        return mType;
    }

    public void setType(int type){

         mType = type;
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
            case 0: return R.mipmap.visa;


            case 1: return R.mipmap.mastercard;


            case 2: return R.mipmap.amex;

            case 3: return R.mipmap.discover;

        }

       return 0;
    }

    public void putChip(){
        mPutChip = true;
        invalidate();
        requestLayout();
    }

    public void checkCardNumberFormat(String cardNumber){
        if(getCardNumberFormat()==1){

            mCardNumber = "**** **** **** " + cardNumber.substring(cardNumber.length() - 4,19);
        }
    }
}
