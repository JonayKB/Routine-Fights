import React, { use, useContext, useEffect, useState } from 'react'
import CommunityEvent from '../models/CommunityEvent'
import { MainContext } from './MainContext'
import { ImageRepository } from '../repositories/ImageRepository'
import { CommunityEventRepository } from '../repositories/CommunityEventRepository'
import Badge from '../models/Badge'
import styles from '../styles/Styles'

type Props = {
    communityEvent: CommunityEvent,
}

const CommunityEventComponent = ({ communityEvent }: Props) => {
    const { token } = useContext(MainContext)
    const [image, setImage] = useState<string>()
    const [badges, setBadges] = useState<Badge[]>([])
    const [showModal, setShowModal] = useState(false)
    const [file, setFile] = useState<File | null>(null)
    const [level, setLevel] = useState<number | "">("")
    const [error, setError] = useState<string>("")



    useEffect(() => {
        async function load() {
            try {
                const [img, b] = await Promise.all([
                    ImageRepository.getImage(communityEvent.image, token),
                    CommunityEventRepository.getBadges(token, communityEvent.id),
                ])
                setImage(img)

                const badgesNew = await Promise.all(
                    b.map(async (badge) => {
                        const img = await getImageBadge(badge)
                        return {
                            ...badge,
                            image: img,
                        }
                    })
                )
                setBadges(badgesNew)


            } catch (err) {
                console.error(err)
            }
        }
        load()
    }, [communityEvent.image, communityEvent.id, token])


    const now = new Date()
    const isActive =
        now >= new Date(communityEvent.startDate) &&
        now <= new Date(communityEvent.finishDate)

    const dynamicStyle: React.CSSProperties = {
        ...styles.communityEventCard,
        backgroundImage: `url(${image})`,
        border: isActive ? "4px solid #824CAF" : "none",
    }

    const openModal = () => {
        setError("")
        setFile(null)
        setLevel("")
        setShowModal(true)
    }
    const closeModal = () => setShowModal(false)
    const getImageBadge = async (badge: Badge) => {
        try {
            return await ImageRepository.getImage(badge.image, token)
        } catch (error) {
            console.error("Error fetching badge image:", error)
            return "https://placehold.co/400"

        }
    }

    const handleBadgeSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        if (level === "" || !file) {
            setError("You must select a file and a level")
            return
        }
        if (badges.some(b => b.level === level)) {
            setError(`Level ${level} already exists`)
            return
        }
        try {
            await CommunityEventRepository.uploadBadge(
                token,
                communityEvent.id,
                file,
                level
            )
            const updated = await CommunityEventRepository.getBadges(token, communityEvent.id)
            const badgesNew = await Promise.all(
                updated.map(async (badge) => {
                    const img = await getImageBadge(badge)
                    return {
                        ...badge,
                        image: img,
                    }
                })
            )
            setBadges(badgesNew)
            closeModal()
        } catch (err) {
            console.error(err)
            setError("Error subiendo la insignia")
        }
    }

    return (
        <>
            <div style={dynamicStyle}>
                <div style={styles.communityEventOverlay}>
                    <div>
                        <h2 style={styles.communityEventTitle}>{communityEvent.name}</h2>
                        <p style={styles.communityEventText}><b>ID:</b> {communityEvent.id}</p>
                        <p style={styles.communityEventText}><b>Start:</b> {new Date(communityEvent.startDate).toLocaleDateString()}</p>
                        <p style={styles.communityEventText}><b>End:</b> {new Date(communityEvent.finishDate).toLocaleDateString()}</p>
                        <p style={styles.communityEventText}><b>Total Required:</b> {communityEvent.totalRequired}</p>
                    </div>
                    <div style={{ display: "flex", alignItems: "center" }}>
                        <div style={styles.communityEventBadgeContainer}>
                            {badges.map(badge => (
                                <div key={badge.id} style={styles.communityEventBadgeWrapper}>
                                    <img
                                        src={badge.image}

                                        alt={badge.level.toString()}
                                        style={styles.communityEventBadge}
                                    />
                                    <h1 style={styles.communityEventBadgeText}>{badge.level}</h1>
                                </div>
                            ))}
                        </div>
                        <button
                            style={styles.communityEventAddButton}
                            onClick={openModal}
                        >
                            + Add Badge
                        </button>
                    </div>
                </div>
            </div>

            {showModal && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modalContent}>
                        <button style={styles.modalCloseButton} onClick={closeModal}>×</button>
                        <form onSubmit={handleBadgeSubmit}>
                            <div style={styles.modalFormGroup}>
                                <label>Level:</label>
                                <input
                                    type="number"
                                    min={1}
                                    value={level}
                                    onChange={e => setLevel(Number(e.target.value))}
                                    style={styles.modalInput}
                                />
                            </div>
                            <div style={styles.modalFormGroup}>
                                <label>Image:</label>
                                <input
                                    type="file"
                                    accept="image/*"
                                    onChange={e => setFile(e.target.files?.[0] || null)}
                                    style={styles.modalInput}
                                />
                            </div>
                            {error && <p style={{ color: 'red', marginBottom: '1rem' }}>{error}</p>}
                            <button type="submit" style={styles.modalSubmitButton}>
                                Upload Badge
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </>
    )
}

export default CommunityEventComponent
