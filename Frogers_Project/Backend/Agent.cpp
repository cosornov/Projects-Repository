#include "Agent.h"

Agent::Agent(int id, QString firstname,QString lastname,QString email, QString password, QString role, QString username) : User(id,firstname,lastname,email,password,role)
{
    this->_username = username;
}

const QString Agent::getUsername(){
    return this->_username;
}

void Agent::setUsername(QString username){
    this->_username = username;
}

