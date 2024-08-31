console.log("hello this is contact javascript");
const baseUrl = "http://localhost:8080";
const viewContactModal = document.getElementById('view_contact_modal');

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true
  };

  const contactModal = new Modal(viewContactModal, options, instanceOptions);

  function openContactModal() {
    contactModal.show();
  }

  function closeContactModal() {
    contactModal.hide();
  }

//   Load contact 
  async function loadContactData(id){
    console.log(id);

    try{
        const data = await ( await fetch(`${baseUrl}/api/contacts/${id}`)).json();
        console.log(data);
        document.querySelector('#contact_name').innerHTML = data.name;
        document.querySelector('#contact_email').innerHTML = data.email;
        const imageElement = document.querySelector('#contact_image');
        imageElement.src = data.picture;

        document.querySelector('#contact_phoneNumber').innerHTML = data.phoneNumber;
        document.querySelector('#contact_address').innerHTML = data.address;
        // document.querySelector('#contact_description').innerHTML = data.description;
        // document.querySelector('#contact_linkedInLink').innerHTML = data.linkedInLink;
        // document.querySelector('#contact_websiteLink').innerHTML = data.websiteLink;

        openContactModal();

    }catch(error){
        console.log(error); 
    }
  }

//   Delete contact 
  async function deleteContact(id){

    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        if (result.isConfirmed) {

            const url = `${baseUrl}/user/contacts/delete/`+ id;
            window.location.replace(url);
        }
      });
  
  }
