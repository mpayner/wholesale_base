let x = document.getElementsByClassName('cart_form');
for(let i=0;i<x.length;i++){
    let quantity = x.item(i).getElementsByTagName('input').item(1);
    let quantity_value = quantity.value;
    quantity.addEventListener('change', function () {
        let old_quantity = quantity_value;
        let button = x.item(i).getElementsByTagName('button').item(0);
        if(this.value!==old_quantity) {
            button.style.display = 'inline';
            button.attributes.getNamedItem('class').value+=' animate-show';
            let form_action  = x.item(i).attributes.getNamedItem('action');
            if(this.value<old_quantity){
                form_action.textContent = '/increaseQuantity';
            }else {
                form_action.textContent = '/decreaseQuantity';
            }
        }else {
            button.style.display = 'none';
        }

        let price = document.getElementsByClassName('good_block').item(i).getElementsByClassName('price').item(0).textContent;
        document.getElementsByClassName('good_block').item(i).getElementsByClassName('summary_price').item(0).textContent = Number(price)*Number(this.value);
    })
}

let cart_items = document.getElementById('cart_items');
let cookie_array = document.cookie.split(';');
let map = new Map();
cookie_array.forEach(i =>{
    let c = i.split('=');
    if(c.at(0).at(0)===' '){
        map.set(c.at(0).slice(1),c.at(1));
    }else {
        map.set(c.at(0),c.at(1));
    }
    if(c.at(0).indexOf('cart_items')!==-1){
        cart_items.textContent=c.at(1);
    }
});
function remove_cart_items() {
    let goods = document.getElementsByClassName('good');
    for (let i=0;i<goods.length;i++) {
        let g = goods.item(i);
        let id = g.getElementsByClassName('good_id').item(0).textContent;
        if(map.has(id)){
            g.getElementsByClassName('add_to_cart_form').item(0).remove();
            let cart_button = document.createElement('a');
            cart_button.href ='/cart';
            cart_button.textContent='кошику';
            let message = document.createElement('p');
            message.textContent='Товар додано до ';
            message.append(cart_button);
            message.style.textAlign = 'center';
            g.append(message);
        }else {
            console.log(g.getElementsByTagName('input').item(1).value);
        }

    }
}
remove_cart_items();

/*let order = document.getElementsByClassName('order');
for (let i in order) {
    let statusList = order[i].getElementsByClassName('status').item(0).getElementsByTagName('select').item(0).getElementsByTagName('option');
    for(let j in statusList){
        statusList[j].textContent = ''
    }
}*/



/*
$(document).ready(function() {
            $('.js-select2').select2({
                    placeholder: "Оберіть зі списку",
                    maximumSelectionLength: 2,
                    language: "ua",
                    width: 'resolve'
            });
});
*/




/*$(function() {
    $('input[type=radio][name=tabs-radio1]').change(function() {
        $('.tabs1 div').hide();
        $(`.tabs1 div[data-name="${this.value}"]`).show();
    })
})

$(function() {
    $('input[type=radio][name=tabs-radio2]').change(function() {
        $('.tabs2 div').hide();
        $(`.tabs2 div[data-name="${this.value}"]`).show();
    })
})*/

