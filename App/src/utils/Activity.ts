export type Activity = {
    id: string;
    name: string;
    description: string;
    image: string;
    timeRate: timeRate;
    timesRequiered: string;
    // category: string;
}

export type ActivityWithStreak = {
    id: string;
    name: string;
    description: string;
    image: string;
    timeRate: timeRate;
    timesRequiered: string;
    streak: number;
    timesRemaining: number;
    // category: string;
}

type timeRate = "daily" | "weekly" | "monthly" | "yearly";