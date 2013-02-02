#include <QApplication>
#include <QDebug>
#include <QSqlDatabase>

#include "Views/LoginView.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    if(DatabaseController::getInstance()->is_connected())
    {

        LoginView VIEW;

        VIEW.show();

        return a.exec();
    }
    return a.exec();
}
