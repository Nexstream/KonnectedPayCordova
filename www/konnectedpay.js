'use strict';

var argscheck = require('cordova/argscheck'),
    exec      = require('cordova/exec');

function KonnectedPay () {};

KonnectedPay.prototype = {

    requestPayment: function (params, success, error)
    {
        argscheck.checkArgs('ofF', 'KonnectedPay.requestPayment', arguments);
        exec(success, error, 'KonnectedPay', 'requestPayment', [params]);
    },

};

module.exports = new KonnectedPay();
