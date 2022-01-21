const btnAdd = document.querySelector('.btn-add');
const btnAddUser = document.querySelector('.btn-add-user');
const form = document.querySelector('.form');
const messages = document.querySelector('.messages');
const message = document.querySelector('.message');


getData();

document.addEventListener('mouseup', function(e) {
  if (!form.contains(e.target)) {
    form.style.display = 'none';
  }
});

btnAdd.addEventListener('click', () => {
  form.style.display = 'block';
});

btnAddUser.addEventListener('click', async () => {

  const first_name = document.getElementById('first_name').value;
  const last_name = document.getElementById('last_name').value;
  const city = document.getElementById('city').value;
  const address = document.getElementById('address').value;
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const country = document.getElementById('country').value;
  const data = { first_name, last_name, country, city, address, email, password };

  const response = await fetch('http://localhost:8080/add', {
    method: 'POST', 
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  }).then(res => res.json())

  console.log(response);
  if(response.message === 'fail'){
    messages.className  = 'error';
    message.textContent = "User with email " + response.email + " already exist.";

    setTimeout(() => {
      message.textContent = '';
      messages.className  = 'messages';
    }, 3000);

  }else {
    messages.className  = 'succes';
    message.textContent = "User with email " + response.email + " added.";

    setTimeout(() => {
      message.textContent = '';
      messages.className  = 'messages';
    }, 3000);

    getData();
  }

});


async function getData() {

  const response = await fetch('http://localhost:8080/data',  {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
        }).then(res => res.json())
        
       createTable(response);

  
  }
  
function createTable(data){
  const table = document.querySelector('.table');
  table.innerHTML = '';

  data.forEach(continent => {
    table.innerHTML += `<div class= "continent">${continent.name}`;

    continent.countryList.forEach(country=> {
      table.innerHTML += `<div class= "country">${country.name}`;

      country.userList.forEach(user => {
        table.innerHTML += `
        <div class= "user">
          <div class="user-content" email = "${user.email}">
            <p>${user.first_name}</p>
            <p>${user.last_name}</p>
            <p>${user.city}</p>
            <p>${user.address}</p>
            <p>${user.email}</p>
            <p>${user.password}</p>
          </div>
          <div class="user-function">
            <button class="btn-delete" onclick="deleteUser('${user.email}')">Delete</delete>
          </div>
        </div>`;
      });
      //close country
      table.innerHTML += `</div>`;
    }); 
  // close continent  
  table.innerHTML += `</div>`;
 }); 
}


function deleteUser(email){

  fetch('http://localhost:8080/delete/?email='+email, {
    method : 'DELETE'
  }).then(res => res.json())
  .then(data => {
    console.log(data);
    if(data.message == 'success'){
      console.log("delete");
      document.querySelectorAll('[email="'+ email +'"]')[0].parentNode.remove();
    }
  })
}


function objToString(obj){
  let str = '';
  for(const [ key, value ] of Object.entries(obj)){
    str += key + ' : ' + value + '\n';
  }
  console.log(str);
} 