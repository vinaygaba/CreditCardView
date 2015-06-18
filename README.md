CreditCard View
==================

![Feature Image](images/Feature Image.png)

CreditCardView is an Android library that allows developers to create the UI which replicates an actual Credit Card.

Storing the credit card details inside the app has become a very common use case seen in a lot of different apps, but it is often represented in a not so intuitive manner. With Android Pay being announced at the recent Google I/O 2015, more apps would require users to input their credit card details. I created this library with the aim of making the process of storing and entering the credit card details more visually appealing to the users of your app.

Screenshots
------------


Features
---------

* Pre-built templates 
* Auto selection of drawables based on the credit card type i.e. Visa, Mastercard,American Express & Discover. Will be adding more soon based on the requests I get
* Auto selection of logo drawable based on the credit card type i.e. Visa, Mastercard and American Express
* Editable and non-editable mode



Setup
------
The library is pushed to Maven Central as an AAR, so you just need to add the followings to your build.gradle file:
```java
dependencies {
    compile ‘com.vinaygaba:creditcardview:1.0.0’
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
        app:cardNumber="5500 0055 5555 5559"
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
Remember put this for custom attribute usage

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

######WORK IN PROGRESS

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
