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

public class CardType {

    public static final int VISA = 0;

    public static final int MASTERCARD = 1;

    public static final int AMERICAN_EXPRESS = 2;

    public static final int DISCOVER = 3;

    public static final int AUTO = 4;

    protected static final String PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$^5[1-5][0-9]{14}$";

    protected static final String PATTERN_MASTER_CARD = "^5[1-5][0-9]{14}$";

    protected static final String PATTERN_AMERICAN_EXPRESS = "^3[47][0-9]{13}$";

    //@formatter:off
    protected static final String PATTERN_DISCOVER = "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$";
    //@formatter:on

}
