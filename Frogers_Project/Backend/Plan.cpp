#include "Plan.h"

Plan::Plan(int id, int price, int hours, int bandwidth, int space, string name)
{
    this->_id=id;
    this->_price=price;
    this->_hours=hours;
    this->_bandwidth=bandwidth;
    this->_space=space;
    this->_name=name;
}
