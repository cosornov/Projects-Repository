#include "Controllers/LoginController.h"
#include "Backend/DatabaseController.h"
#include <QSqlQuery>
//#include <QTextStream>
#include <QSqlDatabase>
#include <QErrorMessage>
#include <QDebug>

LoginController::LoginController()
{
    this->_db = DatabaseController::getInstance();
}

bool LoginController::authenticate(QString name, QString pass){

    QSqlQuery query(this->_db->getDataBase());

    //query based on username provided
    query.prepare("SELECT username, password FROM users WHERE username= :name and password= :pass" );
    query.bindValue(":name", name);
    query.bindValue(":pass", pass);
        if (!query.exec()){
            return false;
        }
        else
            while (query.next()){

                // test is password matched the username found
                if((query.value(0).toString() == name) && (query.value(1).toString() == pass)){
                    return true;
                }
                else{

                    return false;
                }
            }
    return false;
}
