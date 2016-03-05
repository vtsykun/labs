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

MessagesBox = {
    maxId: 0,
    items:{},
};

function printMessage(author, message, date, id) {
    var item = document.getElementsByClassName('message-box left none')[0].cloneNode(true);
    item.classList.remove('none');
    if (author == Cookie.get('nikename')) {
        item.classList.add('left');
        item.getElementsByTagName('b')[0].
            setAttribute('tooltip-l',date+' UTC');
    } else {
        item.classList.add('right');
        item.getElementsByTagName('b')[0].
            setAttribute('tooltip-r',date+' UTC');
    }
    inner = item.getElementsByClassName('message-text')[0];
    if (message == '') {
        item.getElementsByClassName('action')[0].innerHTML = "";
        inner.innerHTML = "Message has been was deleted";
        inner.classList.remove('message-text');
        inner.classList.add('delete');
    } else {
        item.getElementsByTagName('b')[0].innerText = '@'+author;
        item.setAttribute("id", id);
        item.getElementsByTagName('a')[0]
            .setAttribute('onclick','editMessage('+id+')');
        item.getElementsByTagName('a')[1]
            .setAttribute('onclick','deleteMessage('+id+')');
        inner.innerHTML = inner.innerHTML + htmlSpecialChars(message);        
    }
    return item;
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

function sendMessage() {
    var input = document.getElementById('sendText');
    if (input.value.replace(/ /g, '') == '') {
        alert('Message can not be empty');
    } else {
        date = simpleDate();
        MessagesBox.items[(++MessagesBox.maxId)] = 
            {datetime:date,text:input.value,"author":Cookie.get('nikename')};
        item = printMessage(Cookie.get('nikename'), input.value, date, MessagesBox.maxId);
        input.value = "";
        console.log(item);
        scroll = document.getElementsByClassName('scroll-container')[0];
        scroll.appendChild(item);
        scroll.scrollTo(0,scroll.scrollHeight);
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

function simpleDate() {
    s = new Date().toISOString();
    return s.slice(0,10)+" "+s.slice(11,19);
}

function deleteMessage(id) {
    if (confirm("Are you sure what you want to remove this message?")) {
        MessagesBox.items[id].text = "";
        item = document.getElementById(id);
        inner = item.getElementsByClassName('message-text')[0];
        inner.classList.add('delete');
        inner.classList.remove('message-text');
        inner.innerHTML = "Message has been was deleted";
        item.getElementsByClassName('action')[0].innerHTML = "";        
    }
}

function editMessage(id) {
    form = document.getElementById('modal2');
    form.getElementsByTagName('textarea')[0].value = MessagesBox.items[id].text;
    form.getElementsByTagName('button')[0].
        setAttribute('onclick','saveEditMessage('+id+')');
    modalTrigger('modal2');
}

function saveEditMessage(id) {
    now = simpleDate();
    author = Cookie.get('nikename');
    form = document.getElementById('modal2');
    message = form.getElementsByTagName('textarea')[0].value;
    if (message.replace(/ /g, '') == '') {
        alert('Message can not be empty');
    } else {
        item = document.getElementById(id);
        text = item.getElementsByClassName('message-text')[0];
        text.innerHTML = '<b class="author"></b><br>' + htmlSpecialChars(message);
        text.getElementsByTagName('b')[0].innerText = '@'+htmlSpecialChars(author);
        item.getElementsByTagName('b')[0].
            setAttribute('tooltip-l','Last-Modified: '+now+' UTC');
        MessagesBox.items[id].text = message;
        MessagesBox.items[id].date = now;
        modalTrigger('modal2');        
    }
}


window.onload = function(){
    document.getElementById('nikBtn').addEventListener('click', function(){
        Cookie.set('nikename', document.getElementById('nikInput').value);
        modalTrigger('modal1');
    });
    document.getElementById("sendText").addEventListener("keydown", function(e) {
        if (e.keyCode == 13 && e.shiftKey) {
            sendMessage();
        }
    });
    if (Cookie.get('nikename') == '') {
        Cookie.set('nikename','Guest');
    }
    MessagesBox = localStorage.getItem('chat');
    if (MessagesBox == null) {
        MessagesBox = {
            maxId: 0,
            items:{},
        };
    } else {
        MessagesBox = JSON.parse(MessagesBox);
    }
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var json = JSON.parse(xmlhttp.responseText);
            callback(json);
        }
    };
    xmlhttp.open("GET", 'ajax/message.json', true);
    xmlhttp.send();

    function callback(json) {
        scroll = document.getElementsByClassName('scroll-container')[0];
        
        for (var id in json) {
            MessagesBox.items[id] = json[id];
        }

        for (var id in MessagesBox.items) {
            item = MessagesBox.items[id];
            inner = printMessage(item.author, item.text, item.datetime, id);
            scroll.appendChild(inner);
            if (MessagesBox.maxId < id) {
                MessagesBox.maxId = id;
            }
        }
        scroll.scrollTo(0,scroll.scrollHeight);
    };
}

window.onbeforeunload = function(){
    localStorage.setItem("chat", JSON.stringify(MessagesBox));
}