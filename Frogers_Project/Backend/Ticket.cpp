#include "Ticket.h"
#include <QDateTime>

Ticket::Ticket(int id, int userId, int categoryid, int severityid, int assignedtoid, QString status, QString subject, QString description, QDateTime createdat, QDateTime closedat) : DatabaseObject(id)
{
    this->_userid = userId;
    this->_categoryid=categoryid;
    this->_severityid=severityid;
    this->_assignedtoid=assignedtoid;
    this->_status=status;
    this->_subject=subject;
    this->_description=description;
    this->_createdat=createdat;
    this->_closedat=closedat;
}

int Ticket::getUserId(){
    return this->_userid;
}

int Ticket::getCategoryId(){
    return this->_categoryid;
}

int Ticket::getSeverityId(){
    return this->_severityid;
}

int Ticket::getassignedToId(){
    return this->_assignedtoid;
}

QString Ticket::getStatus(){
    return this->_status;
}

QString Ticket::getSubject(){
    return this->_subject;
}

QString Ticket::getDescription(){
    return this->_description;
}

QDateTime  Ticket::getCreatedAt(){
    return this->_createdat;
}

QDateTime  Ticket::getClosedAt(){
    return this->_closedat;
}
