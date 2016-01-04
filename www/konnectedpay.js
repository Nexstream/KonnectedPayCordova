/***
 * Copyright 2015 Nexstream Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
***/


'use strict';

var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');


// Config ----------------------------------------------------------------------

var REQ_FIELDS_REQPAYMENT = [
    "merchantId", "clientSecret", "amount", "transId", "currencyCode",
    "fullName", "email", "userId"
];


// Helpers ---------------------------------------------------------------------

var checkFields = function (reqFields, obj)
{
    reqFields.forEach(function (field) {
        if(obj[field] === undefined) {
            throw new Error("'"+field+"' is undefined");
        }
    })
}


// API implementation ----------------------------------------------------------

var KonnectedPay = function () {};

KonnectedPay.prototype = {

    requestPayment: function (params, success, error)
    {
        try {
            // Check parameters
            argscheck.checkArgs("ofF", "KonnectedPay.requestPayment", arguments);
            checkFields(REQ_FIELDS_REQPAYMENT, params)
            if(typeof params.amount.toFixed !== "function") {
                throw new Error("'amount' must be numeric");
            }

            // Call native SDKs
            exec(success, error, "KonnectedPay", "requestPayment", [{
                merchantId: ""+params.merchantId,
                clientSecret: ""+params.clientSecret,
                amount: params.amount.toFixed(2),
                transId: ""+params.transId,
                currencyCode: ""+params.currencyCode,
                fullName: ""+params.fullName,
                email: ""+params.email,
                userId: ""+params.userId,
                token: ""+params.token,
            }]);
        } catch (e) {
            // Call error callback asynchronously if parameters are incorrect
            setTimeout(function () {
                error({
                    status: "Incorrect usage",
                    desc: e.message,
                });
            }, 0);
        }
    },

};

module.exports = new KonnectedPay();
