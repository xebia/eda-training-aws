import {RouterModule, Routes} from '@angular/router';
import {ListOrderComponent} from "./views/list-order/list-order.component";

const routes: Routes = [
    {
        path: 'order', children: [
            {path: '', component: ListOrderComponent}
        ]
    },
];

export const OrderRouting = RouterModule.forChild(routes);
