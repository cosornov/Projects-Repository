#include "Backend/DatabaseObject.h"

DatabaseObject::DatabaseObject(int id)
{
    this->_id=id;
}

QString DatabaseObject::getType(){
    return this->_type;
}

int DatabaseObject::getId(){
    return this->_id;
}
void DatabaseObject::setId(int id){
    this->_id = id;
}

