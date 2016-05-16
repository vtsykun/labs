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

last_id = 0;

function printMessage(data) {
    var item = document.getElementsByClassName('message-box left none')[0].cloneNode(true);
    item.classList.remove('none');
    item.setAttribute("id", data.message_id);

    var textDate = '';

    if (data.change) {
        textDate += 'Last-Modified: ';
    } 
    textDate += data.stime + ' UTC';

    if (data.nikename == Cookie.get('nikename')) {
        item.classList.add('left');
        item.getElementsByTagName('b')[0].
            setAttribute('tooltip-l', textDate);
    } else {
        item.classList.add('right');
        item.getElementsByTagName('b')[0].
            setAttribute('tooltip-r', textDate);
    }
    inner = item.getElementsByClassName('message-text')[0];
    if (data.body == '') {
        item.getElementsByClassName('action')[0].innerHTML = "";

        inner.innerHTML = "Message has been was deleted";
        inner.classList.remove('message-text');
        inner.classList.add('delete');
    } else {
        item.getElementsByTagName('b')[0].innerText = '@'+data.nikename;
        
        item.getElementsByTagName('a')[0]
            .setAttribute('onclick','editMessage('+data.message_id+')');
        item.getElementsByTagName('a')[1]
            .setAttribute('onclick','deleteMessage('+data.message_id+')');
        inner.innerHTML = inner.innerHTML + '<p>' + htmlSpecialChars(data.body) +'</p>';        
    }

    return item;
}

function refreshMessage(item) {
    var element = document.getElementById(item.message_id);

    if (element == null) {
        element = printMessage(item);
        scroll = document.getElementsByClassName('scroll-container')[0];
        scroll.appendChild(element);
        scroll.scrollTop = scroll.scrollHeight;
        return;
    }

    inner = element.getElementsByClassName('message-text')[0];

    if (inner == null) {
        return;
    }

    if (!item.active) {
        inner.classList.add('delete');
        inner.classList.remove('message-text');
        inner.innerHTML = "Message has been was deleted";
        element.getElementsByClassName('action')[0].innerHTML = "";
        return;  
    }

    if (item.nikename == Cookie.get('nikename')) {
        inner.getElementsByTagName('b')[0].removeAttribute('tooltip-r');        
        inner.getElementsByTagName('b')[0].
            setAttribute('tooltip-l','Last-Modified: ' +  item.stime + ' UTC');
    } else {
        inner.getElementsByTagName('b')[0].removeAttribute('tooltip-l');    
        inner.getElementsByTagName('b')[0].
            setAttribute('tooltip-r','Last-Modified: ' +  item.stime + ' UTC');
    }

    inner.getElementsByTagName('p')[0].innerText = item.body;
}

function editMessage(id) {
    var form = document.getElementById('modal2');
    var element = document.getElementById(id);

    form.getElementsByTagName('textarea')[0].value = 
        element.getElementsByClassName('message-text')[0].getElementsByTagName('p')[0].innerText;
    form.getElementsByTagName('button')[0].
        setAttribute('onclick','saveEditMessage('+id+')');
    modalTrigger('modal2');
}


function saveEditMessage(id) {
    form = document.getElementById('modal2');
    
    var data = new FormData();
    data.append('text', form.getElementsByTagName('textarea')[0].value);

    if (form.getElementsByTagName('textarea')[0].value.replace(/ /g, '') == '') {
        alert('Message can not be empty');
    } else {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", '/api/message/' + id + '/' , true);
        xmlhttp.send(data);
        modalTrigger('modal2');        
    }
}

function deleteMessage(id) {
    if (confirm("Are you sure what you want to remove this message?")) {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", '/api/message/' + id + '/delete' , true);
        xmlhttp.send();
    }
}

function sendMessage() {
    var input = document.getElementById('sendText');
    if (input.value.replace(/ /g, '') == '') {
        alert('Message can not be empty');
        return;
    }

    var data = new FormData();
    data.append('text', input.value);
    data.append('user', Cookie.get('nikename'));

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", '/api/message' , true);
    xmlhttp.send(data);
    input.value = '';
}

function getMessage() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var json = JSON.parse(xmlhttp.responseText);
            for (var id in json) {
                var item = json[id];

                if (item.mbid > last_id) {
                    last_id = item.mbid;
                }
                refreshMessage(item);
                console.log(item);
            }
        }
    };
    xmlhttp.open("GET", '/api/message?last=' + last_id , true);
    xmlhttp.send();
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

function autoGetMessage() {
    setInterval(getMessage, 1000);
}

function htmlSpecialChars(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;")
        .replace(/\n/g, "<br>");
}

window.onload = function(){
    autoGetMessage();
    document.getElementById('nikBtn').addEventListener('click', function(){
        Cookie.set('nikename', document.getElementById('nikInput').value);
        modalTrigger('modal1');
    });
    document.getElementById("sendText").addEventListener("keydown", function(e) {
        if (e.keyCode == 13) {
            sendMessage();
        }
    });

    if (Cookie.get('nikename') == '') {
        Cookie.set('nikename','Guest');
    }

}