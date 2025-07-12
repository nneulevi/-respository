// src/api/book.js
import client from './client'

export const getBooks = (params) => {
    return client.get('/books', { params })
}

export const createBook = (data) => {
    return client.post('/books', data)
}

export const updateBook = (id, data) => {
    return client.put(`/books/${id}`, data)
}

export const deleteBook = (id) => {
    return client.delete(`/books/${id}`)
}