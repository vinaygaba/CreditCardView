CreditCard View
==================
######WORK IN PROGRESS

![Feature Image](images/Feature Image.png)

CreditCardView is an Android library that allows developers to create the UI which replicates an actual Credit Card.

Storing the credit card details inside the app has become a very common use case seen in a lot of different apps, but it is often represented in a not so intuitive manner. With Android Pay being announced at the recent Google I/O 2015, more apps would require users to input their credit card details. I created this library with the aim of making the process of storing and entering the credit card details more visually appealing to the users of your app.

Screenshots
------------
![Screenshots](images/screenshots.png)

Features
---------

* Pre-built card backgrounds to help you get started quickly
* Fully Customizable
* Auto selection of drawables based on the credit card number pattern i.e. Visa, Mastercard,American Express & Discover. Will be adding more soon based on the requests I get
* Auto selection of logo drawable based on the credit card type i.e. Visa, Mastercard and American Express
* Editable and non-editable mode
* 4 different card number formats 



Setup
------
The library is pushed to Maven Central as an AAR, so you just need to add the following to your build.gradle file:
```java
dependencies {
    compile ‘com.vinaygaba:creditcardview:1.0.1’
}
```

Usage
------
Using CreditCardView is extremely easy, this is how you would declare it in the layout xml:
```java
<com.example.vinay.library.CreditCardView
        android:id="@+id/card1"
        android:layout_width="fill_parent"
        android:layout_height="225dp"
        android:background="@drawable/cardbackground_world"
        android:layout_marginBottom="16dp"
        android:layout_marginTop="16dp"
        app:cardNumber="5500005555555559"
        app:cardName="Vinay Gaba"
        app:cardNumberTextColor="#cccccc"
        app:cardNumberFormat="last_four_digits"
        app:cardNameTextColor="#cccccc"
        app:type="auto"
        app:brandLogo="@mipmap/brandlogo"
        app:putChip="true"
        app:expiryDate = "02/22"
        app:expiryDateTextColor="#cccccc"
        app:isEditable="true"
        app:validTillTextColor="#cccccc"
        app:hintTextColor = "#cccccc"
        />
```
Remember to put this for custom attribute usage:

```java

xmlns:app="http://schemas.android.com/apk/res-auto"

```
And this is how you would be adding it programatically in java:

```java
CreditCardView creditCardView= new CreditCardView(this);
```
OR

```java
CreditCardView creditCardView= (CreditCardView)findViewById(R.id.ID_OF_CARD);
```

Attribute Usage & Documentation
-----------------

##### I) android:background

Use this attribute to set the background of the card. This library includes 3 background by default which you can use, but **feel free to put any drawable and use it as the card background as you please**. If you do not want to use your own drawable and want to use the drawables available in the screenshots, do the following:

1)Sky Background

![Sky](images/cardbackground_sky.png)

To use this background,simply use the line:
```
android:background = "@drawable/cardbackground_sky"
```

2)World Background

![World](images/cardbackground_world.png)

To use this background,simply use the following line:
```
android:background = "@drawable/cardbackground_world"
```
3) Plain Background

![Plain](images/cardbackground_plain.png)

This is a customizable plain background where you can change the background color, radius and border color of the card. To use this, add the folowing line:
```
android:background = "@drawable/credit_card"
```

To customize the corner radius of the card, add the following attribute to your dimen.xml file with the attribute name "card_corner_radius":
```
<dimen name="card_corner_radius">size_in_dip</dimen>      //Default value is 10dip
```

To customize the background color and the border color of this card, add the following attributes to your color.xml file:
```
<color name="card_background">color_value</color>        //Default value is #e5e5e5
<color name="card_border">color_value</color>            //Default value is #ffffff
```

##### II) app:cardNumber

Use this attribute to set the card number of the card. 

You can set the value in xml using:
```
app:cardNumber="1234567890123456"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Card Number
crediCardView.setCardNumber("1234567890123456");

//Get Card Number
String cardNumber = crediCardView.getCardNumber();
```

##### III) app:cardNumberTextColor

Use this attribute to set the text color of card number attribute. 

You can set the value in xml using:
```
app:cardNumberTextColor="#ffffff"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Card Number Text Color
creditCardView.setCardNumberTextColor(Color.WHITE);

//Get Card Number Text Color
int color = crediCardView.getCardNumberTextColor();
```

##### IV) app:cardNumberFormat

Use this attribute to set the card number format of card number. There are four different formats supported by the library:

![Card number Format Image](images/cardNumberFormat_Example.png)

1. all_digits - This will display all the numbers of the card number. 
2. masked_all_but_last_four - This will mask all the digits except the last four of the card number. 
3. only_last_four - This will display only the last four digits of the card number.
4. masked_all - This will mask all the digits of the card number. 

You can set the value in xml using:
```
app:cardNumberFormat="all_digits/masked_all_but_last_four/only_last_four/masked_all"    //Use any one format type
```

You can set and get the value of this attribute programmatically using:
```java
//Set Card Number Format. Chooose any one format
creditCardView.setCardNumberFormat(CardNumberFormat.ALL_DIGITS/CardNumberFormat.MASKED_ALL_BUT_LAST_FOUR/CardNumberFormat.ONLY_LAST_FOUR/CardNumberFormat.MASKED_ALL);

//Get Card Number Format
int cardFormat = crediCardView.getCardNumberFormat();
```
*Note: Default value is all_digits*

##### V) app:cardName

Use this attribute to set the card name of the card. 

You can set the value in xml using:
```
app:cardName="John Doe"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Card Number
crediCardView.setCardName("John Doe");

//Get Card Number
String cardNumber = crediCardView.getCardName();
```

##### VI) app:cardNameTextColor

Use this attribute to set the text color of card name attribute. 

You can set the value in xml using:
```
app:cardNameTextColor="#ffffff"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Card Name Text Color
creditCardView.setCardNameTextColor(Color.WHITE);

//Get Card Name Text Color
int color = crediCardView.getCardNamerTextColor();
```

##### VII) app:expiryDate

Use this attribute to set the expiry date of the card in MM/YY or MM/YYYY format. 

You can set the value in xml using:
```
app:expiryDate="01/15"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Expiry Date
crediCardView.setExpiryDate("01/15");

//Get Card Number
String expiryDate = crediCardView.ExpiryDate();
```
##### VIII) app:expiryDateTextColor

Use this attribute to set the text color of expiry date attribute. 

You can set the value in xml using:
```
app:expiryDateTextColor="#ffffff"
```

You can set and get the value of this attribute programmatically using:
```java
//Set Expiry Date Text Color
creditCardView.setExpiryDateTextColor(Color.WHITE);

//Get Expiry Date Text Color
int color = crediCardView.getExpiryDateTextColor();
```

##### IX) app:putChip

Use this attribute if you want the card to display the chip on the card. 

![Put Chip](images/putChip.png)

You can set the value in xml using:
```
app:putChip="true/false"
```

You can set the value of this attribute programmatically using:
```java
//Set Put Chip Value
creditCardView.putChip(true/false);
```

##### IX) app:type

Use this attribute to set the type of the credit card. The library automatically places the corresponding drawable in the bottom right corner based on the type you have selected. Currectly there are 5 different types supported:

1. visa
2. mastercard
3. american_express
4. discover
5. auto - Use auto if u want the library to automatically choose the card type based on the card number you have entered. To know more about the patterns for identifying the card type from the card number, see this [link](http://www.regular-expressions.info/creditcard.html)

You can set the value in xml using:
```
app:type="visa/mastercard/americann_express/discover/auto"
```

You can set the value of this attribute programmatically using:
```java
//Set Card Type.Choose any one card tpe from the following
creditCardView.setType(CardType.VISA/CardType.MASTERCARD/CardType.AMERICAN_EXPRESS/CardType.DISCOVER/CardType.AUTO);

//Get Card Type. 
int type = crediCardView.getType();
```

Contributing
-----------------
Please use the issue tracker to report any bugs or file feature requests. There are a few features that I plan to work on based on the response the library gets, some of them being:

* Tablet Optimization. The current version is not optimized for tablets
* Landscape Optimization. The current version is not optimized for landscape mode
* Credit Card back view to display the CVV number
* Animations and touch callbacks
* Stack View to display multiple cards

I would love to get more people involved in the development of this library. A lot of times people are not sure about how they should be contributing to open source. If you are one of them, this is a great opportunity for you to get involved. You can also reach out to me for any queries that you might have about this library.

Credits
-----------------
Author: Vinay Gaba (vinaygaba@gmail.com)


License
-------

    Copyright 2015 Vinay Gaba

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
