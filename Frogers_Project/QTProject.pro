#-------------------------------------------------
#
# Project created by QtCreator 2012-10-23T12:19:02
#
#-------------------------------------------------

QT       += core gui sql phonon

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = QTProject
TEMPLATE = app


SOURCES += main.cpp\
    Views/LoginView.cpp \
    Backend/DatabaseObject.cpp \
    Controllers/EditAgentController.cpp \
    Views/NewTicketView.cpp \
    Controllers/NewTicketController.cpp \
    Views/EditCustomerView.cpp \
    Controllers/EditCustomerController.cpp \
    Views/EditAgentView.cpp \
    Controllers/LoginController.cpp \
    Backend/DatabaseController.cpp \
    Backend/User.cpp \
    Backend/Ticket.cpp \
    Backend/Comment.cpp \
    Backend/Plan.cpp \
    Backend/Agent.cpp \
    Backend/Customer.cpp \
    Views/CustomerListView.cpp \
    Controllers/CustomerListController.cpp \
    Controllers/DashboardController.cpp \
    Views/DashboardView.cpp \
    Controllers/TicketListController.cpp \
    Views/TicketListV.cpp \
    Views/EditTicketView.cpp \
    Controllers/EditTicketViewController.cpp \
    Controllers/NoteController.cpp \
    Controllers/HistoryViewController.cpp \
    Views/HistoryView.cpp \
    Views/NoteView.cpp

HEADERS  += \
    Views/LoginView.h \
    Backend/DatabaseObject.h \
    Controllers/EditAgentController.h \
    Views/NewTicketView.h \
    Controllers/NewTicketController.h \
    Views/EditCustomerView.h \
    Controllers/EditCustomerController.h \
    Views/EditAgentView.h \
    Controllers/LoginController.h \
    Backend/DatabaseController.h \
    Backend/User.h \
    Backend/Ticket.h \
    Backend/Comment.h \
    Backend/Plan.h \
    Backend/Agent.h \
    Backend/Customer.h \
    Views/CustomerListView.h \
    Controllers/CustomerListController.h \
    Controllers/DashboardController.h \
    Views/DashboardView.h \
    Controllers/TicketListController.h \
    Views/TicketListV.h \
    Views/EditTicketView.h \
    Controllers/EditTicketViewController.h \
    Controllers/NoteController.h \
    Controllers/HistoryViewController.h \
    Views/HistoryView.h \
    Views/NoteView.h

FORMS    += \
    Views/LoginView.ui \
    Views/EditCustomerView.ui \
    Views/CustomerListView.ui \
    Views/EditAgentView.ui \
    Views/NewTicketView.ui \
    Views/TicketListV.ui \
    Views/HistoryView.ui \
    Views/NoteView.ui \
    Views/EditTicketView.ui \
    Views/DashboardView.ui

RESOURCES += \
    Resources/Resources.qrc
