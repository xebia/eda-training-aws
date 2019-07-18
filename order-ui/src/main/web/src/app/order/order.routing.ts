import {RouterModule, Routes} from '@angular/router';
import {ListOrderComponent} from "./views/list-order/list-order.component";
import {PlaceOrderComponent} from "./views/place-order/place-order.component";

const routes: Routes = [
    {
        path: 'order', children: [
            {path: '', component: ListOrderComponent},
            {path: 'place', component: PlaceOrderComponent},
        ]
    },
];

export const OrderRouting = RouterModule.forChild(routes);
