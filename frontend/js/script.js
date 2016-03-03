Cookie = {
set: function(name, value) {
        var expires = new Date();
        expires.setDate(expires.getDate() + 31);
        document.cookie = name + "=" + value + "; expires=" + expires.toUTCString() + "; path=/";
    },
get: function(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i=0; i<ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1);
            if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
        }
        return "";
    }
}

function copy(author, message) {
    var item = document.getElementsByClassName('message-box left none')[0].cloneNode(true);
    item.classList.remove('none');
    item.getElementsByTagName('b')[0].innerText = '@'+author;
    var inner = item.getElementsByClassName('message-text')[0];
    inner.innerHTML = inner.innerHTML + message;
    return item;
}

function htmlSpecialChars(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/ /g, "&nbsp")
        .replace(/'/g, "&#039;");
 }

function sendMessage() {
    var input = document.getElementById('sendText');
    if (input.value == '') {
        alert('Сообщение не должно быть пустым');
    } else {
        item = copy(Cookie.get('nikename'), htmlSpecialChars(input.value));
        input.value = "";
        console.log(item);
        document.getElementsByClassName('scroll-container')[0].appendChild(item);        
    }
}


function modalTrigger(modal) {
    sirm = document.getElementById(modal);
    if (sirm.style.visibility == "visible") {
        sirm.style.visibility = "hidden";
        document.body.classList.remove('modal-open');
    } else {
        sirm.style.visibility = "visible";
        document.body.classList.add('modal-open'); 
    }
}

window.onload = function(){
    document.getElementById('nikBtn').addEventListener('click', function(){
        Cookie.set('nikename', document.getElementById('nikInput').value);
        modalTrigger('modal1');
    });
}