export type Activity = {
    id: string;
    name: string;
    description: string;
    image: string;
    timeRate: string;
    timesRequiered: string;
    // category: string;
}

export type ActivityWithStreak = {
    id: string;
    name: string;
    description: string;
    image: string;
    timeRate: string;
    timesRequiered: string;
    streak: number;
    // category: string;
}