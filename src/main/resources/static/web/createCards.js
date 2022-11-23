const app = Vue.createApp({
    data(){
        return{
            clientName: [],
            clientLastName: [],
            clientCards: [],
            cardHolder: [],
            creditCards: [],
            debitCards:[],
            typeOfCard: "",
            colorOfCard: "",
        }
    },
    created(){
        this.loadData()
    },
    mounted(){

    },
    methods: {
        loadData(url){
            axios.get('/api/clients/current')
            .then((response => {
                this.clientName = response.data.firstName
                this.clientLastName = response.data.lastName
                this.cardHolder = this.clientName + " " + this.clientLastName
                this.clientCards = response.data.cards
                this.creditCards = this.clientCards.filter(type => type.type == "CREDIT")
                this.debitCards = this.clientCards.filter(type => type.type == "DEBIT")
            }))
        },
        logout(){
            axios.post('/api/logout')
            .then((response =>{
                window.location.assign("./index.html")
            }))
        },
        createCard(){
            axios.post('/api/clients/current/cards', `type=${this.typeOfCard}&color=${this.colorOfCard}`)
            .then((response =>{
                window.location.assign("./cards.html")
            }))
            .catch(()  => {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: `Please, complete all the fields to procede.`,
                })
            })
        },
        redirectToCards(){
            window.location.assign("./cards.html")
        },

    },
    computed:{

    },
}).mount('#app')