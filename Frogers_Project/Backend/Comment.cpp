#include "Comment.h"

Comment::Comment(int id, int userid,int ticketid, QString body, QDateTime createdat) : DatabaseObject(id){
    this->_ticketid=ticketid;
    this->_userid=userid;
    this->_body=body;
    this->_createdat=createdat;
}

int Comment::getTicketID(){
    return this->_ticketid;
}

int Comment::getuserID(){
    return this->_userid;
}

QString Comment::getBody(){
    return this->_body;
}

QDateTime Comment::getCreatedAt(){
    return this->_createdat;
}

void Comment::setTicketID(int id){
    this->_ticketid=id;
}

void Comment::setUserID(int id){
    this->_userid=id;
}

void Comment::setBody(QString body){
    this->_body=body;
}

void Comment::setCreatedAt(QDateTime date){
    this->_createdat=date;
}
