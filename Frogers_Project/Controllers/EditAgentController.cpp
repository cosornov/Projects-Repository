#include "Controllers/EditAgentController.h"
#include <QRegExp>

EditAgentController::EditAgentController()
{
    this->_db = DatabaseController::getInstance();
}

bool EditAgentController::validate(QString test){
    if(test.isEmpty() || test.isNull())
        return false;
    return true;
}

void EditAgentController::save(Agent save){
    //write to database with this method
    this->_db->saveAgent(save);
}

Agent EditAgentController::getCurrentAgent(){
    return this->_db->getAgent(this->_db->getAgent());
}

bool EditAgentController::emailExists(QString email){
    return (this->_db->emailExists(email)) && (getCurrentAgent().getEmail().compare(email)!=0);
}

bool EditAgentController::userExists(QString username){
    return (this->_db->userExists(username)) && (getCurrentAgent().getUsername().compare(username)!=0);
}

bool EditAgentController::validEmail(QString email){
    //checks to make sure email is of a valid format
    QRegExp testEmail("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$");
    return testEmail.exactMatch(email);
}
