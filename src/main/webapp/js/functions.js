

function countText(e) {
    if (!document.getElementById("form:message")) {
        console.log('somehow');
        var textValue = document.getElementById("maintab:form:message").value;

    } else {
        var textValue = document.getElementById("form:message").value;

    }
    if (!document.getElementById("form:optout")) {
        var optOut = document.getElementById("maintab:form:optout").value.length + 1;
    } else {
        var optOut = document.getElementById("form:optout").value.length + 1;
    }

    var typedChars = textValue.length + optOut;
    console.log("Total Charaters typed: ", textValue.length);
    console.log("Total Charaters processed: ", typedChars);
    console.log("optout legth", optOut);
    var MAX = 1024;
    if (typedChars >= MAX) {
        textValue = textValue.substring(0, MAX - optOut);
        document.getElementById("form:message").value = textValue;
    }
    var numMap =
            [
                {count: 160, num: 1}, {count: 268, num: 2},
                {count: 402, num: 3}, {count: 536, num: 4},
                {count: 670, num: 5}, {count: 804, num: 6},
                {count: 938, num: 7}, {count: 1024, num: 8}
            ];
    var currentPage = numMap.filter(page => page.count >= typedChars)[0];
    //console.log("Page:", currentPage.num);
    if (!document.getElementById("form:remLen")) {
        document.getElementById("maintab:form:remLen").value = `(${currentPage.count - typedChars}/${currentPage.count}) ${currentPage.num}`;

    } else {
        document.getElementById("form:remLen").value = `(${currentPage.count - typedChars}/${currentPage.count}) ${currentPage.num}`;

    }
}


                

function updateSubAccount() {
    
    var current = document.getElementById("form:currbal").innerHTML;
    console.log(current);
    var creditsval = document.getElementById("form:altavalue").value;
    var action = document.getElementById("form:action_label").innerHTML;
    var total;
    if (action === 'Add Credits') {
        total = parseInt(current) + parseInt(creditsval);
    } else if (action === "Deduct Credits") {
        total = parseInt(current) - parseInt(creditsval);
    } else {
        console.log("No Selected Option");

    }
    document.getElementById("form:newval").value = total;



}
//function textCounter() {
//
////    if (document.getElementById("form:message").value.length > 1010) {
////        document.getElementById("form:message").value = document.getElementById("form:message").value.substring(0, 1010);
////
////    } else
////        // document.getElementById("form:remLen").value = maxlimit - document.getElementById("form:message").value.length;
////        var smscount = 0;
////
//
//        if (document.getElementById("form:message").value.length > 1024)
//        document.getElementById("form:message").value = document.getElementById("form:message").value.substring(0, 1024);
//    else
////document.getElementById("form:remLen").value = maxlimit - document.getElementById("form:message").value.length;
//        var smscount = 0;
//
//    if (document.getElementById("form:message").value.length > 160)
//    {
//        var msgLength = document.getElementById("form:message").value.length;
//        if (document.getElementById("form:message").value.length <= 268) {
////            alert("on Second message")
//            smscount = 2;
//        } else if (document.getElementById("form:message").value.length <= 402)
//            smscount = 3;
//        else if (document.getElementById("form:message").value.length <= 536)
//            smscount = 4;
//        else if (document.getElementById("form:message").value.length <= 670)
//            smscount = 5;
//        else if (document.getElementById("form:message").value.length <= 804)
//            smscount = 6;
//        else if (document.getElementById("form:message").value.length <= 938)
//            smscount = 7;
//        else if (document.getElementById("form:message").value.length <= 1010)//1072
////            if (document.getElementById("form:message").value.length >= 1010) {
////                if (!document.getElementById("form:message").value.toString().endsWith(" ")) {
////                    document.getElementById("form:message").value = document.getElementById("form:message").value.substring(0, 1009);
//////console.log("Execting>>>>>");
////                }
////            }
////        console.log("Messsage is equal or over 1010");
////        var optout = document.getElementById("form:optout").value.length;
////        titaltesx = msgLength + optout;
////        console.log("Original sms count" + titaltesx);
//            smscount = 8;
//    } else
//    {
//        var msgLength = document.getElementById("form:message").value.length;
//        var msg = document.getElementById("form:message").value;
//        console.log("Original message length" + msgLength);
//        if (msg.toString().endsWith(' ')) {
//            optoutmsg = document.getElementById("form:optout").value.length;
//            // console.log("This string is has a space at the end");
//        } else {
//            optoutmsg = document.getElementById("form:optout").value.length;
//            if (optoutmsg > 1) {
//                if (msg.length < 1) {
//                    //console.log("This message does not end with ticks and length is less than one" + msg.toString().length);
//                    optoutmsg = document.getElementById("form:optout").value.length;
//
//                } else {
//                    //console.log("This message does not end with ticks and length is " + msg.toString().length);
//                    optoutmsg = document.getElementById("form:optout").value.length + 1;
//                }
//            }
//        }
//        smscount = 1;
//        smschrscount = optoutmsg + document.getElementById("form:message").value.length;
//        var optout = document.getElementById("form:optout").value.length;
//        titaltesx = msgLength + optout;
//        console.log("Original sms count" + titaltesx);
//        console.log("total sms count" + smschrscount);
//    }
//
//
//if (smscount < 2) {
//
//    ///document.getElementById("form:remLen").value = "(" + (document.getElementById("form:mumLen").value - smschrscount) + "/" + document.getElementById("form:mumLen").value+")" + smscount;
//
//    document.getElementById("form:remLen").value = "(" + (document.getElementById("form:mumLen").value - document.getElementById("form:message").value.length) + "/" + document.getElementById("form:mumLen").value + ")" + smscount;
//} else
//    document.getElementById("form:remLen").value = "(" + ((134 * smscount) - document.getElementById("form:message").value.length) + "/" + 134 + ")" + smscount;
//}
//function textCounter2() {
//    if (document.getElementById("maintab:formCont:message").value.length > 1024)
//        document.getElementById("maintab:formCont:message").value = document.getElementById("maintab:formCont:message").value.substring(0, 1024);
//    else
////document.getElementById("form:remLen").value = maxlimit - document.getElementById("form:message").value.length;
//        var smscount = 0;
//    if (document.getElementById("maintab:formCont:message").value.length > 160)
//    {
//        if (document.getElementById("maintab:formCont:message").value.length <= 268) {
////            alert("on Second message")
//            smscount = 2;
//        } else if (document.getElementById("maintab:formCont:message").value.length <= 402)
//            smscount = 3;
//        else if (document.getElementById("maintab:formCont:message").value.length <= 536)
//            smscount = 4;
//        else if (document.getElementById("maintab:formCont:message").value.length <= 670)
//            smscount = 5;
//        else if (document.getElementById("maintab:formCont:message").value.length <= 804)
//            smscount = 6;
//        else if (document.getElementById("maintab:formCont:message").value.length <= 938)
//            smscount = 7;
//        else if (document.getElementById("maintab:formCont:message").value.length <= 1024)//1072
//            smscount = 8;
//    } else
//    {
//        smscount = 1;
//    }
//    if (smscount < 2)
//        document.getElementById("maintab:formCont:remLen").value = "(" + (document.getElementById("maintab:formCont:mumLen").value - document.getElementById("maintab:formCont:message").value.length) + "/" + document.getElementById("maintab:formCont:mumLen").value + ")" + smscount;
//    else
//        document.getElementById("maintab:formCont:remLen").value = "(" + ((134 * smscount) - document.getElementById("maintab:formCont:message").value.length) + "/" + 134 + ")" + smscount;
//}
//
//function makeValue() {
//    document.form.to.value = document.form.to.value + document.form.names.value + '\n';
//}
//
//function checkFromAddr() {
//    var fromAddr = document.form.from.value;
//    if (fromAddr.length === 0 || fromAddr === '0')
//    {
//        alert("Please select SMS from Address.");
//        return false;
//    }
//}
//
//function OnChange() {
//    if (document.form.contacts.value === 'groups') {
//        for (i = document.form.names.length - 1; i > 0; i--) {
//            document.form.names.options[i] = null;
//        }
//        document.form.names.options[0] = new Option('--Select One--', '');
//    } else if (document.form.contacts.value === 'contacts') {
//        for (i = document.form.names.length - 1; i > 0; i--) {
//            document.form.names.options[i] = null;
//        }
//        document.form.names.options[0] = new Option('--Select One--', '');
//    } else {
//        document.form.names.options[0] = new Option('--Select One--', '');
//    }
//}
//
