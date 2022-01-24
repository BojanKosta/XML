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
  var html = '';
  data.forEach(continent => {
    html += `<div class= "continent"><h2>${continent.name}</h2>`;

    continent.countryList.forEach(country=> {
      html += `<div class= "country"><h3>${country.name}</h3>`;

      country.userList.forEach(user => {
        html += `
        <div class= "user">
          <div class="user-content" email = "${user.email}">
              <div class="items-1">
                <p>First Name: ${user.first_name}</p>
                <p>Last Name: ${user.last_name}</p>
                <p>CIty: ${user.city}</p>
              </div>  
              <div class="items-2">
                <p>Address: ${user.address}</p>
                <p>Email: ${user.email}</p>
                <p>Password: ${user.password}</p>
              </div>  
          </div>
          <div class="user-function">
            <div class="center">
            <button class="btn-delete" onclick="deleteUser('${user.email}')">Delete</delete>
            </div>
          </div>
        </div>`;
      });
      //close country
      html += `</div>`;
    }); 
  // close continent  
  html += `</div>`;
 }); 
 table.innerHTML = html;
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