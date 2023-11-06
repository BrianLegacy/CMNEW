/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function loadgroups() {
//alert(PF('selectWV').getSelectedValue());
    var kevol = PF('selectWV2').getSelectedValue();
    var kevol2 = PF('addresslist').jq.val();
    PF('addresslist').jq.val(kevol + "\n" + kevol2);
}



function textCounter() {
     //var senderid = document.getElementById("form:frm").value;
      console.log("Selected sender ID "+senderid);
      
    if (document.getElementById("form:message").value.length > 1024)
        document.getElementById("form:message").value = document.getElementById("form:message").value.substring(0, 1024);
    else
//document.getElementById("form:remLen").value = maxlimit - document.getElementById("form:message").value.length;
        var smscount = 0;
    if (document.getElementById("form:message").value.length > 160)
    {
        if (document.getElementById("form:message").value.length <= 268) {
alert("on Second message");
            smscount = 2;
        } else if (document.getElementById("form:message").value.length <= 402)
            smscount = 3;
        else if (document.getElementById("form:message").value.length <= 536)
            smscount = 4;
        else if (document.getElementById("form:message").value.length <= 670)
            smscount = 5;
        else if (document.getElementById("form:message").value.length <= 804)
            smscount = 6;
        else if (document.getElementById("form:message").value.length <= 938)
            smscount = 7;
        else if (document.getElementById("form:message").value.length <= 1024)//1072
            smscount = 8;
       
    } else
    {
        smscount = 1;
    }
    if (smscount < 2){
        console.log("herre were");
        document.getElementById("form:remLen").value = "(" + (document.getElementById("form:mumLen").value - document.getElementById("form:message").value.length) + "/" + document.getElementById("form:mumLen").value + ")" + smscount;
    } else
        document.getElementById("form:remLen").value = "(" + ((134 * smscount) - document.getElementById("form:message").value.length) + "/" + 134 + ")" + smscount;
}











function makeValue() {
    document.form.to.value = document.form.to.value + document.form.names.value + '\n';
}

function checkFromAddr() {
    var fromAddr = document.form.from.value;
    if (fromAddr.length == 0 || fromAddr == '0')
    {
        alert("Please select SMS from Address.");
        return false;
    }
}

function OnChange() {
    if (document.form.contacts.value == 'groups') {
        for (i = document.form.names.length - 1; i > 0; i--) {
            document.form.names.options[i] = null;
        }
        document.form.names.options[0] = new Option('--Select One--', '');
    } else if (document.form.contacts.value == 'contacts') {
        for (i = document.form.names.length - 1; i > 0; i--) {
            document.form.names.options[i] = null;
        }
        document.form.names.options[0] = new Option('--Select One--', '');
    } else {
        document.form.names.options[0] = new Option('--Select One--', '');
    }
}