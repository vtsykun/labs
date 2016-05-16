from django.db import connection


def get_message(last):
    cursor = connection.cursor()
    cursor.execute('select * from v_message where mbid > %s order by message_id', [last])
    desc = cursor.description
    return [
        dict(zip([col[0] for col in desc], row))
        for row in cursor.fetchall()
    ]

def create_message(user, text):
    cursor = connection.cursor()
    cursor.execute('select createmessage(%s, %s)', [user, text])

def up_message(mess_id, text):
    cursor = connection.cursor()
    cursor.execute('insert into message_body (body, update_date, message_id) values (%s, now(), %s)', [text, mess_id])

def del_message(mess_id):
    cursor = connection.cursor()
    cursor.execute('update message set active = false where id = %s', [mess_id])